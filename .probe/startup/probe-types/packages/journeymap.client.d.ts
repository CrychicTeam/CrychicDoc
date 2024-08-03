declare module "packages/journeymap/client/ui/minimap/$Selectable" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $Selectable {

 "tick"(): void
 "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
 "renderBorder"(arg0: $GuiGraphics$Type, arg1: integer): void
 "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
 "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}

export namespace $Selectable {
const SELECTED_COLOR: integer
const UNSELECTED_COLOR: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Selectable$Type = ($Selectable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Selectable_ = $Selectable$Type;
}}
declare module "packages/journeymap/client/service/webmap/$Webmap" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $Webmap {
static readonly "INSTANCE": $Webmap


public "start"(): void
public "getLogger"(): $Logger
public "stop"(): void
public "getPort"(): integer
public "getStarted"(): boolean
public "setPort"(arg0: integer): void
public "setStarted"(arg0: boolean): void
get "logger"(): $Logger
get "port"(): integer
get "started"(): boolean
set "port"(value: integer)
set "started"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Webmap$Type = ($Webmap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Webmap_ = $Webmap$Type;
}}
declare module "packages/journeymap/client/model/$EntityHelper" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$EntityHelper$EntityDistanceComparator, $EntityHelper$EntityDistanceComparator$Type} from "packages/journeymap/client/model/$EntityHelper$EntityDistanceComparator"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$EntityHelper$EntityMapComparator, $EntityHelper$EntityMapComparator$Type} from "packages/journeymap/client/model/$EntityHelper$EntityMapComparator"
import {$EntityHelper$EntityDTODistanceComparator, $EntityHelper$EntityDTODistanceComparator$Type} from "packages/journeymap/client/model/$EntityHelper$EntityDTODistanceComparator"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityHelper {
static "entityDistanceComparator": $EntityHelper$EntityDistanceComparator
static "entityDTODistanceComparator": $EntityHelper$EntityDTODistanceComparator
static "entityMapComparator": $EntityHelper$EntityMapComparator

constructor()

public static "getEntitiesNearby"(arg0: string, arg1: integer, arg2: boolean, ...arg3: ($Class$Type<(any)>)[]): $List<($EntityDTO)>
public static "getAnimalsNearby"(): $List<($EntityDTO)>
public static "buildEntityIdMap"(arg0: $List$Type<(any)>, arg1: boolean): $Map<(string), ($EntityDTO)>
public static "getPlayersNearby"(): $List<($EntityDTO)>
public static "getMobsNearby"(): $List<($EntityDTO)>
public static "getVillagersNearby"(): $List<($EntityDTO)>
public static "getBoundingBox"(arg0: $Player$Type, arg1: double, arg2: double): $AABB
public static "getIconTextureLocation"(arg0: $Entity$Type): $ResourceLocation
public static "isPassive"(arg0: $LivingEntity$Type): boolean
get "animalsNearby"(): $List<($EntityDTO)>
get "playersNearby"(): $List<($EntityDTO)>
get "mobsNearby"(): $List<($EntityDTO)>
get "villagersNearby"(): $List<($EntityDTO)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHelper$Type = ($EntityHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHelper_ = $EntityHelper$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeTextureStitchedEvent" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$TextureStitchEvent$Post, $TextureStitchEvent$Post$Type} from "packages/net/minecraftforge/client/event/$TextureStitchEvent$Post"

export class $ForgeTextureStitchedEvent implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onTextureStitched"(arg0: $TextureStitchEvent$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeTextureStitchedEvent$Type = ($ForgeTextureStitchedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeTextureStitchedEvent_ = $ForgeTextureStitchedEvent$Type;
}}
declare module "packages/journeymap/client/cartography/render/$SurfaceRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$BlockCoordIntPair, $BlockCoordIntPair$Type} from "packages/journeymap/client/model/$BlockCoordIntPair"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$BaseRenderer, $BaseRenderer$Type} from "packages/journeymap/client/cartography/render/$BaseRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $SurfaceRenderer extends $BaseRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor()

public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "render"(arg0: $ComparableNativeImage$Type, arg1: $NativeImage$Type, arg2: $RegionData$Type, arg3: $ChunkMD$Type): boolean
public "render"(arg0: $ComparableNativeImage$Type, arg1: $NativeImage$Type, arg2: $RegionData$Type, arg3: $ChunkMD$Type, arg4: integer, arg5: boolean): boolean
public "getBlockHeight"(arg0: $ChunkMD$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): integer
public "getBlockHeight"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): integer
public "getSurfaceBlockHeight"(arg0: $ChunkMD$Type, arg1: integer, arg2: integer, arg3: $BlockCoordIntPair$Type, arg4: integer): integer
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceRenderer$Type = ($SurfaceRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceRenderer_ = $SurfaceRenderer$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$GridEditor" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $GridEditor extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $JmUI$Type)

public "getTileSample"(arg0: $MapType$Type): $Texture
public "m_7856_"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridEditor$Type = ($GridEditor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridEditor_ = $GridEditor$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$PopupMenuEvent$WaypointPopupMenuEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/api/display/$Waypoint"
import {$PopupMenuEvent, $PopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $PopupMenuEvent$WaypointPopupMenuEvent extends $PopupMenuEvent {

constructor()
constructor(arg0: $ModPopupMenu$Type, arg1: $IFullscreen$Type, arg2: $Waypoint$Type)

public "getWaypoint"(): $Waypoint
public "getListenerList"(): $ListenerList
get "waypoint"(): $Waypoint
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuEvent$WaypointPopupMenuEvent$Type = ($PopupMenuEvent$WaypointPopupMenuEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuEvent$WaypointPopupMenuEvent_ = $PopupMenuEvent$WaypointPopupMenuEvent$Type;
}}
declare module "packages/journeymap/client/cartography/color/$BlockStateColor" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

export class $BlockStateColor implements $Comparable<($BlockStateColor)> {


public "compareTo"(arg0: $BlockStateColor$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateColor$Type = ($BlockStateColor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateColor_ = $BlockStateColor$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$MinimapOptions" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MiniMapProperties, $MiniMapProperties$Type} from "packages/journeymap/client/properties/$MiniMapProperties"

export class $MinimapOptions extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $MiniMapProperties$Type)

public "close"(): void
public "init"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): void
public "onClose"(): void
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "tick"(): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinimapOptions$Type = ($MinimapOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinimapOptions_ = $MinimapOptions$Type;
}}
declare module "packages/journeymap/client/render/map/$TileDrawStepCache" {
import {$TileDrawStep, $TileDrawStep$Type} from "packages/journeymap/client/render/map/$TileDrawStep"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"

export class $TileDrawStepCache {


public static "clear"(): void
public static "size"(): long
public static "instance"(): $Cache<(string), ($TileDrawStep)>
public static "getOrCreate"(arg0: $MapType$Type, arg1: $RegionCoord$Type, arg2: integer, arg3: boolean, arg4: integer, arg5: integer, arg6: integer, arg7: integer): $TileDrawStep
public static "setContext"(arg0: $File$Type, arg1: $MapType$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TileDrawStepCache$Type = ($TileDrawStepCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TileDrawStepCache_ = $TileDrawStepCache$Type;
}}
declare module "packages/journeymap/client/ui/component/$Label" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$DrawUtil$HAlign, $DrawUtil$HAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$HAlign"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $Label extends $Button {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: integer, arg1: string, ...arg2: (any)[])

public "fitWidth"(arg0: $Font$Type): void
public "setHAlign"(arg0: $DrawUtil$HAlign$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getFitWidth"(arg0: $Font$Type): integer
set "hAlign"(value: $DrawUtil$HAlign$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Label$Type = ($Label);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Label_ = $Label$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$PolygonsKt" {
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $PolygonsKt {


public static "polygonsGet"(arg0: $RouteHandler$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolygonsKt$Type = ($PolygonsKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolygonsKt_ = $PolygonsKt$Type;
}}
declare module "packages/journeymap/client/world/$JmBlockAccess" {
import {$ModelDataManager, $ModelDataManager$Type} from "packages/net/minecraftforge/client/model/data/$ModelDataManager"
import {$LevelLightEngine, $LevelLightEngine$Type} from "packages/net/minecraft/world/level/lighting/$LevelLightEngine"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ColorResolver, $ColorResolver$Type} from "packages/net/minecraft/world/level/$ColorResolver"
import {$ClipContext, $ClipContext$Type} from "packages/net/minecraft/world/level/$ClipContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ClipBlockStateContext, $ClipBlockStateContext$Type} from "packages/net/minecraft/world/level/$ClipBlockStateContext"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $JmBlockAccess extends $Enum<($JmBlockAccess)> implements $BlockAndTintGetter {
static readonly "INSTANCE": $JmBlockAccess


public static "values"(): ($JmBlockAccess)[]
public static "valueOf"(arg0: string): $JmBlockAccess
public "getBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getFluidState"(arg0: $BlockPos$Type): $FluidState
public "getLightEngine"(): $LevelLightEngine
public "getBiome"(arg0: $BlockPos$Type): $Biome
public "getBiome"(arg0: $BlockPos$Type, arg1: $Biome$Type): $Biome
public "getWorld"(): $Level
public "canSeeSky"(arg0: $BlockPos$Type): boolean
public "getHeight"(): integer
public "getBlockTint"(arg0: $BlockPos$Type, arg1: $ColorResolver$Type): integer
public "getRawBrightness"(arg0: $BlockPos$Type, arg1: integer): integer
public "getMaxLightLevel"(): integer
public "getShade"(arg0: $Direction$Type, arg1: boolean): float
public "getBrightness"(arg0: $LightLayer$Type, arg1: $BlockPos$Type): integer
public "clipWithInteractionOverride"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $BlockPos$Type, arg3: $VoxelShape$Type, arg4: $BlockState$Type): $BlockHitResult
public "getLightEmission"(arg0: $BlockPos$Type): integer
public "getMinBuildHeight"(): integer
public "clip"(arg0: $ClipContext$Type): $BlockHitResult
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "getMaxBuildHeight"(): integer
public "getBlockEntity"<T extends $BlockEntity>(arg0: $BlockPos$Type, arg1: $BlockEntityType$Type<(T)>): $Optional<(T)>
public "getBlockStates"(arg0: $AABB$Type): $Stream<($BlockState)>
public "isBlockInLine"(arg0: $ClipBlockStateContext$Type): $BlockHitResult
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public "getBlockFloorHeight"(arg0: $VoxelShape$Type, arg1: $Supplier$Type<($VoxelShape$Type)>): double
public "getBlockFloorHeight"(arg0: $BlockPos$Type): double
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
public "getExistingBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getModelDataManager"(): $ModelDataManager
get "lightEngine"(): $LevelLightEngine
get "world"(): $Level
get "height"(): integer
get "maxLightLevel"(): integer
get "minBuildHeight"(): integer
get "maxBuildHeight"(): integer
get "sectionsCount"(): integer
get "minSection"(): integer
get "maxSection"(): integer
get "modelDataManager"(): $ModelDataManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JmBlockAccess$Type = (("instance")) | ($JmBlockAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JmBlockAccess_ = $JmBlockAccess$Type;
}}
declare module "packages/journeymap/client/api/util/$PolygonHelper" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$MapPolygon, $MapPolygon$Type} from "packages/journeymap/client/api/model/$MapPolygon"
import {$Area, $Area$Type} from "packages/java/awt/geom/$Area"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MapPolygonWithHoles, $MapPolygonWithHoles$Type} from "packages/journeymap/client/api/model/$MapPolygonWithHoles"

export class $PolygonHelper {

constructor()

public static "createChunkPolygonForWorldCoords"(arg0: integer, arg1: integer, arg2: integer): $MapPolygon
public static "createChunksPolygon"(arg0: $Collection$Type<($ChunkPos$Type)>, arg1: integer): $List<($MapPolygonWithHoles)>
public static "createPolygonFromArea"(arg0: $Area$Type, arg1: integer): $List<($MapPolygonWithHoles)>
public static "toArea"(arg0: $MapPolygon$Type): $Area
public static "createChunkPolygon"(arg0: integer, arg1: integer, arg2: integer): $MapPolygon
public static "createChunksArea"(arg0: $Collection$Type<($ChunkPos$Type)>): $Area
public static "createBlockRect"(arg0: $BlockPos$Type, arg1: $BlockPos$Type): $MapPolygon
public static "classifyAndGroup"(arg0: $List$Type<($MapPolygon$Type)>): $List<($MapPolygonWithHoles)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolygonHelper$Type = ($PolygonHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolygonHelper_ = $PolygonHelper$Type;
}}
declare module "packages/journeymap/client/api/impl/$ThemeToolbarDisplayFactory" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$ThemeToolbar, $ThemeToolbar$Type} from "packages/journeymap/client/ui/theme/$ThemeToolbar"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IThemeToolBar, $IThemeToolBar$Type} from "packages/journeymap/client/api/display/$IThemeToolBar"
import {$ThemeFactory, $ThemeFactory$Type} from "packages/journeymap/client/api/impl/$ThemeFactory"
import {$IThemeButton, $IThemeButton$Type} from "packages/journeymap/client/api/display/$IThemeButton"
import {$CustomToolBarBuilder, $CustomToolBarBuilder$Type} from "packages/journeymap/client/api/display/$CustomToolBarBuilder"

export class $ThemeToolbarDisplayFactory extends $ThemeFactory implements $CustomToolBarBuilder {

constructor(arg0: $Theme$Type, arg1: $Fullscreen$Type)

public "getNewToolbar"(...arg0: ($IThemeButton$Type)[]): $IThemeToolBar
public "getToolbarList"(): $List<($ThemeToolbar)>
get "toolbarList"(): $List<($ThemeToolbar)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeToolbarDisplayFactory$Type = ($ThemeToolbarDisplayFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeToolbarDisplayFactory_ = $ThemeToolbarDisplayFactory$Type;
}}
declare module "packages/journeymap/client/data/$WorldData" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$WorldData$DimensionProvider, $WorldData$DimensionProvider$Type} from "packages/journeymap/client/data/$WorldData$DimensionProvider"

export class $WorldData extends $CacheLoader<($Class), ($WorldData)> {

constructor()

public static "getLegacyServerName"(): string
public static "isHardcoreAndMultiplayer"(): boolean
public "load"(arg0: $Class$Type<(any)>): $WorldData
public static "getRegion"(): string
public static "getSystemTime"(): string
public static "getRealGameTime"(): string
public static "getGameTime"(): string
public static "getMoonPhase"(): string
public "getTTL"(): long
public static "getDimension"(): string
public static "isDay"(arg0: long): boolean
public static "isNight"(arg0: long): boolean
public static "getLightLevel"(): string
public static "getDimensionProviders"(): $List<($WorldData$DimensionProvider)>
public static "getDimensionProviders"(arg0: $List$Type<(string)>): $List<($WorldData$DimensionProvider)>
public static "getSafeDimensionName"(arg0: $WorldData$DimensionProvider$Type): string
public static "getWorldName"(arg0: $Minecraft$Type): string
get "legacyServerName"(): string
get "hardcoreAndMultiplayer"(): boolean
get "region"(): string
get "systemTime"(): string
get "realGameTime"(): string
get "gameTime"(): string
get "moonPhase"(): string
get "tTL"(): long
get "dimension"(): string
get "lightLevel"(): string
get "dimensionProviders"(): $List<($WorldData$DimensionProvider)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldData$Type = ($WorldData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldData_ = $WorldData$Type;
}}
declare module "packages/journeymap/client/render/map/$GridRenderer" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$File, $File$Type} from "packages/java/io/$File"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $GridRenderer {
 "mouseX": integer
 "mouseY": integer
 "fullscreen": $Fullscreen

constructor(arg0: $Context$UI$Type)

public "clear"(): void
public "move"(arg0: double, arg1: double): void
public "getGridSize"(): integer
public "getWindowPosition"(arg0: $Point2D$Double$Type): $Point2D$Double
public "isOnScreen"(arg0: double, arg1: double, arg2: integer, arg3: integer): boolean
public "isOnScreen"(arg0: double, arg1: double): boolean
public "isOnScreen"(arg0: $Rectangle2D$Double$Type): boolean
public "isOnScreen"(arg0: $Point2D$Double$Type): boolean
public "setViewPort"(arg0: $Rectangle2D$Double$Type): void
public static "clearDebugMessages"(): void
public static "setEnabled"(arg0: boolean): void
public "draw"(arg0: $GuiGraphics$Type, arg1: $List$Type<(any)>, arg2: double, arg3: double, arg4: double, arg5: double): void
public "draw"(arg0: $GuiGraphics$Type, arg1: $List$Type<(any)>, arg2: $Fullscreen$Type, arg3: integer, arg4: integer, arg5: double, arg6: double, arg7: double, arg8: double): void
public "draw"(arg0: $GuiGraphics$Type, arg1: double, arg2: double, arg3: double, arg4: double, ...arg5: ($DrawStep$Type)[]): void
public "draw"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: double, arg4: double, arg5: boolean): void
public "center"(arg0: $File$Type, arg1: $MapType$Type, arg2: double, arg3: double, arg4: integer): boolean
public "center"(): boolean
public "getWidth"(): integer
public "getHeight"(): integer
public "getUIState"(): $UIState
public "getRenderSize"(): integer
public "getMapType"(): $MapType
public "setGridSize"(arg0: integer): void
public "getDisplay"(): $Context$UI
public "getCenterPixelOffset"(): $Point2D$Double
public "getBlockPixelInGrid"(arg0: double, arg1: double): $Point2D$Double
public "getBlockPixelInGrid"(arg0: $BlockPos$Type): $Point2D$Double
public "shiftWindowPosition"(arg0: double, arg1: double, arg2: integer, arg3: integer): $Point2D
public "updateTiles"(arg0: $MapType$Type, arg1: integer, arg2: boolean, arg3: integer, arg4: integer, arg5: boolean, arg6: double, arg7: double): void
public "setZoom"(arg0: integer): boolean
public "getZoom"(): integer
public "getWorldDir"(): $File
public "updateRotation"(arg0: $PoseStack$Type, arg1: double): void
public "clearGlErrors"(arg0: boolean): void
public "getPixel"(arg0: double, arg1: double): $Point2D$Double
public "updateUIState"(arg0: boolean): void
public "getBlockBounds"(): $AABB
public static "addDebugMessage"(arg0: string, arg1: string): void
public "getBlockAtPixel"(arg0: $Point2D$Double$Type): $BlockPos
public static "removeDebugMessage"(arg0: string, arg1: string): void
public "getCenterBlockZ"(): double
public "getMatrixPosition"(arg0: $Point2D$Double$Type): $Point2D$Double
public "ensureOnScreen"(arg0: $Point2D$Type): void
public "getCenterBlockX"(): double
public "hasUnloadedTile"(arg0: boolean): boolean
public "hasUnloadedTile"(): boolean
public "setContext"(arg0: $File$Type, arg1: $MapType$Type): void
get "gridSize"(): integer
set "viewPort"(value: $Rectangle2D$Double$Type)
set "enabled"(value: boolean)
get "width"(): integer
get "height"(): integer
get "uIState"(): $UIState
get "renderSize"(): integer
get "mapType"(): $MapType
set "gridSize"(value: integer)
get "display"(): $Context$UI
get "centerPixelOffset"(): $Point2D$Double
set "zoom"(value: integer)
get "zoom"(): integer
get "worldDir"(): $File
get "blockBounds"(): $AABB
get "centerBlockZ"(): double
get "centerBlockX"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridRenderer$Type = ($GridRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridRenderer_ = $GridRenderer$Type;
}}
declare module "packages/journeymap/client/ui/component/$ScrollPane$ScrollPaneEntry" {
import {$ObjectSelectionList$Entry, $ObjectSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$ObjectSelectionList$Entry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ScrollPane, $ScrollPane$Type} from "packages/journeymap/client/ui/component/$ScrollPane"

export class $ScrollPane$ScrollPaneEntry extends $ObjectSelectionList$Entry<($ScrollPane$ScrollPaneEntry)> {

constructor(arg0: $ScrollPane$Type, arg1: $ScrollPane$Type, arg2: $Button$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "getNarration"(): $Component
get "narration"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollPane$ScrollPaneEntry$Type = ($ScrollPane$ScrollPaneEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollPane$ScrollPaneEntry_ = $ScrollPane$ScrollPaneEntry$Type;
}}
declare module "packages/journeymap/client/api/model/$WrappedEntity" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$WeakReference, $WeakReference$Type} from "packages/java/lang/ref/$WeakReference"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $WrappedEntity {

 "getOwner"(): $Entity
 "setColor"(arg0: integer): void
 "getPosition"(): $Vec3
 "getDimension"(): $ResourceKey<($Level)>
 "getProfession"(): string
 "getUnderground"(): boolean
 "getHeading"(): double
 "isInvisible"(): boolean
 "getHostile"(): boolean
 "setEntityToolTips"(arg0: $List$Type<($Component$Type)>): void
 "getBiome"(): $Biome
 "setDisable"(arg0: boolean): void
 "getPlayerName"(): string
 "getCustomName"(): $Component
 "isNpc"(): boolean
 "isSneaking"(): boolean
 "getEntityToolTips"(): $List<($Component)>
 "isPassiveAnimal"(): boolean
 "getEntityLivingRef"(): $WeakReference<($LivingEntity)>
 "getChunkPos"(): $BlockPos
 "getEntityIconLocation"(): $ResourceLocation
 "setEntityIconLocation"(arg0: $ResourceLocation$Type): void
 "getColor"(): integer
 "setCustomName"(arg0: string): void
 "setCustomName"(arg0: $Component$Type): void
 "isDisabled"(): boolean
 "getEntityId"(): string
}

export namespace $WrappedEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrappedEntity$Type = ($WrappedEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrappedEntity_ = $WrappedEntity$Type;
}}
declare module "packages/journeymap/client/model/$ImageHolder" {
import {$RegionTexture, $RegionTexture$Type} from "packages/journeymap/client/texture/$RegionTexture"

export class $ImageHolder {


public "getImageTimestamp"(): long
public "writeNextIO"(): boolean
public "toString"(): string
public "clear"(): void
public "getTexture"(): $RegionTexture
public "hasTexture"(): boolean
get "imageTimestamp"(): long
get "texture"(): $RegionTexture
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageHolder$Type = ($ImageHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageHolder_ = $ImageHolder$Type;
}}
declare module "packages/journeymap/client/model/$ImageSet" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $ImageSet {

constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clear"(): void
public "writeToDisk"(arg0: boolean): integer
public "writeToDiskAsync"(arg0: boolean): integer
public "updatedSince"(arg0: $MapType$Type, arg1: long): boolean
public "getImage"(arg0: $MapType$Type): $NativeImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageSet$Type = ($ImageSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageSet_ = $ImageSet$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeRenderLevelLastEvent" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$RenderLevelStageEvent, $RenderLevelStageEvent$Type} from "packages/net/minecraftforge/client/event/$RenderLevelStageEvent"

export class $ForgeRenderLevelLastEvent implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onRenderWorldLastEvent"(arg0: $RenderLevelStageEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeRenderLevelLastEvent$Type = ($ForgeRenderLevelLastEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeRenderLevelLastEvent_ = $ForgeRenderLevelLastEvent$Type;
}}
declare module "packages/journeymap/client/log/$StatTimer" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $StatTimer {
static readonly "NS": double


public "getName"(): string
public static "get"(arg0: string, arg1: integer): $StatTimer
public static "get"(arg0: string): $StatTimer
public static "get"(arg0: string, arg1: integer, arg2: integer): $StatTimer
public "start"(): $StatTimer
public "stop"(): double
public "reset"(): void
public "elapsed"(): double
public "cancel"(): void
public "report"(): void
public "getElapsedLimitReachedCount"(): integer
public "getSimpleReportString"(): string
public "hasReachedElapsedLimit"(): boolean
public "getElapsedLimitWarningsRemaining"(): integer
public static "getReportByTotalTime"(arg0: string, arg1: string): $List<(string)>
public "getLogReportString"(): string
public static "getDisposable"(arg0: string): $StatTimer
public static "getDisposable"(arg0: string, arg1: integer): $StatTimer
public static "resetAll"(): void
public static "getReport"(): string
public "getReportString"(): string
public "stopAndReport"(): string
get "name"(): string
get "elapsedLimitReachedCount"(): integer
get "simpleReportString"(): string
get "elapsedLimitWarningsRemaining"(): integer
get "logReportString"(): string
get "reportString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatTimer$Type = ($StatTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatTimer_ = $StatTimer$Type;
}}
declare module "packages/journeymap/client/api/model/$MapImage" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MapImage {

constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: float)
constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer)
constructor(arg0: $NativeImage$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: float)
constructor(arg0: $NativeImage$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "setColor"(arg0: integer): $MapImage
public "setOpacity"(arg0: float): $MapImage
public "getTextureWidth"(): integer
public "getTextureHeight"(): integer
public "getAnchorY"(): double
public "setAnchorY"(arg0: double): $MapImage
public "setAnchorX"(arg0: double): $MapImage
public "getAnchorX"(): double
public "getRotation"(): integer
public "setRotation"(arg0: integer): $MapImage
public "getTextureX"(): integer
public "getTextureY"(): integer
public "setDisplayHeight"(arg0: double): $MapImage
public "getImageLocation"(): $ResourceLocation
public "getDisplayWidth"(): double
public "getDisplayHeight"(): double
public "setDisplayWidth"(arg0: double): $MapImage
public "centerAnchors"(): $MapImage
public "getColor"(): integer
public "getImage"(): $NativeImage
public "getOpacity"(): float
set "color"(value: integer)
set "opacity"(value: float)
get "textureWidth"(): integer
get "textureHeight"(): integer
get "anchorY"(): double
set "anchorY"(value: double)
set "anchorX"(value: double)
get "anchorX"(): double
get "rotation"(): integer
set "rotation"(value: integer)
get "textureX"(): integer
get "textureY"(): integer
set "displayHeight"(value: double)
get "imageLocation"(): $ResourceLocation
get "displayWidth"(): double
get "displayHeight"(): double
set "displayWidth"(value: double)
get "color"(): integer
get "image"(): $NativeImage
get "opacity"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapImage$Type = ($MapImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapImage_ = $MapImage$Type;
}}
declare module "packages/journeymap/client/texture/$IgnSkin" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $IgnSkin {

constructor()

public static "getFaceImage"(arg0: $UUID$Type, arg1: string): $NativeImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IgnSkin$Type = ($IgnSkin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IgnSkin_ = $IgnSkin$Type;
}}
declare module "packages/journeymap/client/properties/$WaypointProperties" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$CustomField, $CustomField$Type} from "packages/journeymap/common/properties/config/$CustomField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$StringField, $StringField$Type} from "packages/journeymap/common/properties/config/$StringField"
import {$ClientPropertiesBase, $ClientPropertiesBase$Type} from "packages/journeymap/client/properties/$ClientPropertiesBase"
import {$FloatField, $FloatField$Type} from "packages/journeymap/common/properties/config/$FloatField"

export class $WaypointProperties extends $ClientPropertiesBase implements $Comparable<($WaypointProperties)> {
readonly "managerEnabled": $BooleanField
readonly "beaconEnabled": $BooleanField
readonly "showTexture": $BooleanField
readonly "showStaticBeam": $BooleanField
readonly "showRotatingBeam": $BooleanField
readonly "showName": $BooleanField
readonly "showDistance": $BooleanField
readonly "autoHideLabel": $BooleanField
readonly "showDeviationLabel": $BooleanField
readonly "disableStrikeThrough": $BooleanField
readonly "boldLabel": $BooleanField
readonly "fontScale": $FloatField
readonly "textureSmall": $BooleanField
readonly "shaderBeacon": $BooleanField
readonly "maxDistance": $IntegerField
readonly "minDistance": $IntegerField
readonly "createDeathpoints": $BooleanField
readonly "autoRemoveDeathpoints": $BooleanField
readonly "autoRemoveDeathpointDistance": $IntegerField
readonly "autoRemoveTempWaypoints": $IntegerField
readonly "showDeathpointlabel": $BooleanField
readonly "fullscreenDoubleClickToCreate": $BooleanField
readonly "teleportCommand": $CustomField
readonly "dateFormat": $StringField
readonly "timeFormat": $StringField
readonly "managerDimensionFocus": $BooleanField

constructor()

public "getName"(): string
public "compareTo"(arg0: $WaypointProperties$Type): integer
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointProperties$Type = ($WaypointProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointProperties_ = $WaypointProperties$Type;
}}
declare module "packages/journeymap/client/model/$RegionImageSet" {
import {$ImageHolder, $ImageHolder$Type} from "packages/journeymap/client/model/$ImageHolder"
import {$RegionImageSet$Key, $RegionImageSet$Key$Type} from "packages/journeymap/client/model/$RegionImageSet$Key"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$ImageSet, $ImageSet$Type} from "packages/journeymap/client/model/$ImageSet"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $RegionImageSet extends $ImageSet {

constructor(arg0: $RegionImageSet$Key$Type)

public "getOldestTimestamp"(): long
public "hasChunkUpdates"(): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getRegionCoord"(): $RegionCoord
public "finishChunkUpdates"(): void
public "getChunkImage"(arg0: $ChunkMD$Type, arg1: $MapType$Type): $ComparableNativeImage
public "setChunkImage"(arg0: $ChunkMD$Type, arg1: $MapType$Type, arg2: $ComparableNativeImage$Type): void
public "getHolder"(arg0: $MapType$Type): $ImageHolder
get "oldestTimestamp"(): long
get "regionCoord"(): $RegionCoord
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionImageSet$Type = ($RegionImageSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionImageSet_ = $RegionImageSet$Type;
}}
declare module "packages/journeymap/client/render/draw/$OverlayDrawStep" {
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"

export interface $OverlayDrawStep extends $DrawStep {

 "setTitlePosition"(arg0: $Point2D$Double$Type): void
 "getBounds"(): $Rectangle2D$Double
 "isOnScreen"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: $GridRenderer$Type, arg4: double): boolean
 "setEnabled"(arg0: boolean): void
 "getOverlay"(): $Overlay
 "getDisplayOrder"(): integer
 "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
 "getModId"(): string
}

export namespace $OverlayDrawStep {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverlayDrawStep$Type = ($OverlayDrawStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverlayDrawStep_ = $OverlayDrawStep$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Minimap" {
import {$Theme$Minimap$MinimapSquare, $Theme$Minimap$MinimapSquare$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSquare"
import {$Theme$Minimap$MinimapCircle, $Theme$Minimap$MinimapCircle$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapCircle"

export class $Theme$Minimap {
 "circle": $Theme$Minimap$MinimapCircle
 "square": $Theme$Minimap$MinimapSquare

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Minimap$Type = ($Theme$Minimap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Minimap_ = $Theme$Minimap$Type;
}}
declare module "packages/journeymap/client/api/impl/$ModPopupMenuImpl$MenuItem" {
import {$ModPopupMenu$Action, $ModPopupMenu$Action$Type} from "packages/journeymap/client/api/display/$ModPopupMenu$Action"
import {$ModPopupMenuImpl$SubMenuAction, $ModPopupMenuImpl$SubMenuAction$Type} from "packages/journeymap/client/api/impl/$ModPopupMenuImpl$SubMenuAction"

export class $ModPopupMenuImpl$MenuItem {

constructor(arg0: string, arg1: $ModPopupMenu$Action$Type)
constructor(arg0: string, arg1: $ModPopupMenuImpl$SubMenuAction$Type, arg2: boolean)

public "isAutoCloseable"(): boolean
public "getSubMenuAction"(): $ModPopupMenuImpl$SubMenuAction
public "getAction"(): $ModPopupMenu$Action
public "getLabel"(): string
get "autoCloseable"(): boolean
get "subMenuAction"(): $ModPopupMenuImpl$SubMenuAction
get "action"(): $ModPopupMenu$Action
get "label"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModPopupMenuImpl$MenuItem$Type = ($ModPopupMenuImpl$MenuItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModPopupMenuImpl$MenuItem_ = $ModPopupMenuImpl$MenuItem$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawStep$Pass" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DrawStep$Pass extends $Enum<($DrawStep$Pass)> {
static readonly "Object": $DrawStep$Pass
static readonly "Text": $DrawStep$Pass
static readonly "Tooltip": $DrawStep$Pass


public static "values"(): ($DrawStep$Pass)[]
public static "valueOf"(arg0: string): $DrawStep$Pass
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawStep$Pass$Type = (("tooltip") | ("text") | ("object")) | ($DrawStep$Pass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawStep$Pass_ = $DrawStep$Pass$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Control" {
import {$Theme$Control$ButtonSpec, $Theme$Control$ButtonSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Control$ButtonSpec"

export class $Theme$Control {
 "button": $Theme$Control$ButtonSpec
 "toggle": $Theme$Control$ButtonSpec

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Control$Type = ($Theme$Control);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Control_ = $Theme$Control$Type;
}}
declare module "packages/journeymap/client/api/display/$CustomToolBarBuilder" {
import {$IThemeButton$Action, $IThemeButton$Action$Type} from "packages/journeymap/client/api/display/$IThemeButton$Action"
import {$IThemeToolBar, $IThemeToolBar$Type} from "packages/journeymap/client/api/display/$IThemeToolBar"
import {$IThemeButton, $IThemeButton$Type} from "packages/journeymap/client/api/display/$IThemeButton"

export interface $CustomToolBarBuilder {

 "getThemeButton"(arg0: string, arg1: string, arg2: $IThemeButton$Action$Type): $IThemeButton
 "getThemeButton"(arg0: string, arg1: string, arg2: string, arg3: $IThemeButton$Action$Type): $IThemeButton
 "getNewToolbar"(...arg0: ($IThemeButton$Type)[]): $IThemeToolBar
 "getThemeToggleButton"(arg0: string, arg1: string, arg2: string, arg3: $IThemeButton$Action$Type): $IThemeButton
 "getThemeToggleButton"(arg0: string, arg1: string, arg2: $IThemeButton$Action$Type): $IThemeButton
}

export namespace $CustomToolBarBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomToolBarBuilder$Type = ($CustomToolBarBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomToolBarBuilder_ = $CustomToolBarBuilder$Type;
}}
declare module "packages/journeymap/client/event/handlers/$PopupMenuEventHandler" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $PopupMenuEventHandler {

constructor()

public "onWaypointPopupMenu"(arg0: $ModPopupMenu$Type, arg1: string, arg2: $Fullscreen$Type): void
public "onFullscreenPopupMenu"(arg0: $ModPopupMenu$Type, arg1: $Fullscreen$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuEventHandler$Type = ($PopupMenuEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuEventHandler_ = $PopupMenuEventHandler$Type;
}}
declare module "packages/journeymap/client/api/event/$FullscreenMapEvent" {
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"

export class $FullscreenMapEvent extends $ClientEvent {
readonly "type": $ClientEvent$Type
readonly "dimension": $ResourceKey<($Level)>
readonly "timestamp": long


public "getLocation"(): $BlockPos
public "getLevel"(): $ResourceKey<($Level)>
get "location"(): $BlockPos
get "level"(): $ResourceKey<($Level)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenMapEvent$Type = ($FullscreenMapEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenMapEvent_ = $FullscreenMapEvent$Type;
}}
declare module "packages/journeymap/client/api/display/$Context" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Context {

}

export namespace $Context {
const probejs$$marker: never
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
declare module "packages/journeymap/client/api/event/$DeathWaypointEvent" {
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"

export class $DeathWaypointEvent extends $ClientEvent {
readonly "location": $BlockPos
readonly "type": $ClientEvent$Type
readonly "dimension": $ResourceKey<($Level)>
readonly "timestamp": long

constructor(arg0: $BlockPos$Type, arg1: $ResourceKey$Type<($Level$Type)>)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeathWaypointEvent$Type = ($DeathWaypointEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeathWaypointEvent_ = $DeathWaypointEvent$Type;
}}
declare module "packages/journeymap/client/io/nbt/$CustomChunkReader$ProcessedChunk" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $CustomChunkReader$ProcessedChunk extends $Record {

constructor(chunk: $LevelChunk$Type, light: ((((byte)[])[])[])[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "chunk"(): $LevelChunk
public "light"(): ((((byte)[])[])[])[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomChunkReader$ProcessedChunk$Type = ($CustomChunkReader$ProcessedChunk);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomChunkReader$ProcessedChunk_ = $CustomChunkReader$ProcessedChunk$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$ServerOptionsManager" {
import {$OptionScreen, $OptionScreen$Type} from "packages/journeymap/client/ui/option/$OptionScreen"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ServerPropertyType, $ServerPropertyType$Type} from "packages/journeymap/common/network/data/$ServerPropertyType"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ServerOptionsManager extends $OptionScreen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $JmUI$Type)
constructor(arg0: $JmUI$Type, arg1: string, arg2: $List$Type<($Category$Type)>)

public "renderBackground"(arg0: $GuiGraphics$Type): void
public "m_7856_"(): void
public "setData"(arg0: $ServerPropertyType$Type, arg1: string, arg2: string): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "resize"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerOptionsManager$Type = ($ServerOptionsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerOptionsManager_ = $ServerOptionsManager$Type;
}}
declare module "packages/journeymap/client/api/impl/$ThemeButtonDisplayFactory" {
import {$ThemeButton, $ThemeButton$Type} from "packages/journeymap/client/ui/theme/$ThemeButton"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IThemeButton$Action, $IThemeButton$Action$Type} from "packages/journeymap/client/api/display/$IThemeButton$Action"
import {$ThemeButtonDisplay, $ThemeButtonDisplay$Type} from "packages/journeymap/client/api/display/$ThemeButtonDisplay"
import {$ThemeFactory, $ThemeFactory$Type} from "packages/journeymap/client/api/impl/$ThemeFactory"
import {$IThemeButton, $IThemeButton$Type} from "packages/journeymap/client/api/display/$IThemeButton"

export class $ThemeButtonDisplayFactory extends $ThemeFactory implements $ThemeButtonDisplay {

constructor(arg0: $Theme$Type)

public "addThemeButton"(arg0: string, arg1: string, arg2: string, arg3: $IThemeButton$Action$Type): $IThemeButton
public "addThemeButton"(arg0: string, arg1: string, arg2: $IThemeButton$Action$Type): $IThemeButton
public "addThemeToggleButton"(arg0: string, arg1: string, arg2: boolean, arg3: $IThemeButton$Action$Type): $IThemeButton
public "addThemeToggleButton"(arg0: string, arg1: string, arg2: string, arg3: boolean, arg4: $IThemeButton$Action$Type): $IThemeButton
public "getThemeButtonList"(): $List<($ThemeButton)>
get "themeButtonList"(): $List<($ThemeButton)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeButtonDisplayFactory$Type = ($ThemeButtonDisplayFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeButtonDisplayFactory_ = $ThemeButtonDisplayFactory$Type;
}}
declare module "packages/journeymap/client/mod/impl/$CodeChickenLibMod" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

/**
 * 
 * @deprecated
 */
export class $CodeChickenLibMod implements $IModBlockHandler, $IBlockColorProxy {

constructor()

public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public "initialize"(arg0: $BlockMD$Type): void
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodeChickenLibMod$Type = ($CodeChickenLibMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodeChickenLibMod_ = $CodeChickenLibMod$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawImageStep" {
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$ImageOverlay, $ImageOverlay$Type} from "packages/journeymap/client/api/display/$ImageOverlay"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BaseOverlayDrawStep, $BaseOverlayDrawStep$Type} from "packages/journeymap/client/render/draw/$BaseOverlayDrawStep"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"

export class $DrawImageStep extends $BaseOverlayDrawStep<($ImageOverlay)> {
readonly "overlay": T

constructor(arg0: $ImageOverlay$Type)

public "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawImageStep$Type = ($DrawImageStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawImageStep_ = $DrawImageStep$Type;
}}
declare module "packages/journeymap/client/texture/$ImageUtil" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $ImageUtil {

constructor()

public static "getSizedImage"(arg0: integer, arg1: integer, arg2: $NativeImage$Type, arg3: boolean): $NativeImage
public static "recolorImage"(arg0: $NativeImage$Type, arg1: integer): $NativeImage
public static "getScaledImage"(arg0: float, arg1: $NativeImage$Type, arg2: boolean): $NativeImage
public static "clearAndClose"(arg0: $NativeImage$Type): void
public static "getAlpha"(arg0: integer): integer
public static "getComparableSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $NativeImage$Type, arg5: boolean): $ComparableNativeImage
public static "closeSafely"(arg0: $NativeImage$Type): void
public static "getNewBlankImage"(arg0: integer, arg1: integer): $NativeImage
public static "getSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $NativeImage$Type, arg5: $NativeImage$Type, arg6: boolean): $NativeImage
public static "getSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $NativeImage$Type, arg5: boolean): $NativeImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageUtil$Type = ($ImageUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageUtil_ = $ImageUtil$Type;
}}
declare module "packages/journeymap/client/ui/component/$Slot" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"

export class $Slot extends $AbstractSelectionList$Entry<(any)> {

constructor()

public "contains"(arg0: $SlotMetadata$Type<(any)>): boolean
public "getLastPressed"(): $SlotMetadata<(any)>
public "setEnabled"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "getColumnWidth"(): integer
public "getMetadata"(): $Collection<($SlotMetadata)>
public "displayHover"(arg0: boolean): void
public "getCurrentTooltip"(): $SlotMetadata<(any)>
public "getChildSlots"(arg0: integer, arg1: integer): $List<(any)>
get "lastPressed"(): $SlotMetadata<(any)>
set "enabled"(value: boolean)
get "columnWidth"(): integer
get "metadata"(): $Collection<($SlotMetadata)>
get "currentTooltip"(): $SlotMetadata<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Slot$Type = ($Slot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Slot_ = $Slot$Type;
}}
declare module "packages/journeymap/client/ui/component/$IConfigFieldHolder" {
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"

export interface $IConfigFieldHolder<T extends $ConfigField<(any)>> {

 "getConfigField"(): T

(): T
}

export namespace $IConfigFieldHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigFieldHolder$Type<T> = ($IConfigFieldHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigFieldHolder_<T> = $IConfigFieldHolder$Type<(T)>;
}}
declare module "packages/journeymap/client/api/option/$Config" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Config<T> {

 "get"(): T
 "set"(arg0: T): $Config<(T)>
}

export namespace $Config {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Config$Type<T> = ($Config<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Config_<T> = $Config$Type<(T)>;
}}
declare module "packages/journeymap/client/$InternalStateHandler" {
import {$ClientState, $ClientState$Type} from "packages/journeymap/common/network/data/model/$ClientState"

export class $InternalStateHandler {

constructor()

public "reset"(): void
public "isJourneyMapServerConnection"(): boolean
public "isModdedServerConnection"(): boolean
public "getMaxRenderDistance"(): integer
public "useServerFullscreenBiomes"(): boolean
public "setJourneyMapServerConnection"(arg0: boolean): void
public "setWaypointsAllowed"(arg0: boolean): void
public "setModdedServerConnection"(arg0: boolean): void
public "setMaxRenderDistance"(arg0: integer): void
public "setMultiplayerOptionsAllowed"(arg0: boolean): void
public "setShowInGameBeacons"(arg0: boolean): void
public "setReadOnlyServerAdmin"(arg0: boolean): void
public "setStates"(arg0: $ClientState$Type): void
public "setTeleportEnabled"(arg0: boolean): void
public "isServerAdmin"(): boolean
public "canServerAdmin"(): boolean
public "isTeleportEnabled"(): boolean
public "isAllowDeathPoints"(): boolean
public "isWaypointsAllowed"(): boolean
public "isMultiplayerOptionsAllowed"(): boolean
public "canShowInGameBeacons"(): boolean
public "isReadOnlyServerAdmin"(): boolean
public "isExpandedRadarEnabled"(): boolean
get "journeyMapServerConnection"(): boolean
get "moddedServerConnection"(): boolean
get "maxRenderDistance"(): integer
set "journeyMapServerConnection"(value: boolean)
set "waypointsAllowed"(value: boolean)
set "moddedServerConnection"(value: boolean)
set "maxRenderDistance"(value: integer)
set "multiplayerOptionsAllowed"(value: boolean)
set "showInGameBeacons"(value: boolean)
set "readOnlyServerAdmin"(value: boolean)
set "states"(value: $ClientState$Type)
set "teleportEnabled"(value: boolean)
get "serverAdmin"(): boolean
get "teleportEnabled"(): boolean
get "allowDeathPoints"(): boolean
get "waypointsAllowed"(): boolean
get "multiplayerOptionsAllowed"(): boolean
get "readOnlyServerAdmin"(): boolean
get "expandedRadarEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalStateHandler$Type = ($InternalStateHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalStateHandler_ = $InternalStateHandler$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSpec" {
import {$Theme$ImageSpec, $Theme$ImageSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ImageSpec"
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"
import {$Theme$LabelSpec, $Theme$LabelSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$LabelSpec"

export class $Theme$Minimap$MinimapSpec {
 "margin": integer
 "labelTop": $Theme$LabelSpec
 "labelTopInside": boolean
 "labelBottom": $Theme$LabelSpec
 "labelBottomInside": boolean
 "compassLabel": $Theme$LabelSpec
 "compassPoint": $Theme$ImageSpec
 "compassPointLabelPad": integer
 "compassPointOffset": double
 "compassShowNorth": boolean
 "compassShowSouth": boolean
 "compassShowEast": boolean
 "compassShowWest": boolean
 "waypointOffset": double
 "reticle": $Theme$ColorSpec
 "reticleHeading": $Theme$ColorSpec
 "reticleThickness": double
 "reticleHeadingThickness": double
 "reticleOffsetOuter": integer
 "reticleOffsetInner": integer
 "frame": $Theme$ColorSpec
 "prefix": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Minimap$MinimapSpec$Type = ($Theme$Minimap$MinimapSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Minimap$MinimapSpec_ = $Theme$Minimap$MinimapSpec$Type;
}}
declare module "packages/journeymap/client/data/$DataCache" {
import {$RegionImageSet$Key, $RegionImageSet$Key$Type} from "packages/journeymap/client/model/$RegionImageSet$Key"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$DrawImageStep, $DrawImageStep$Type} from "packages/journeymap/client/render/draw/$DrawImageStep"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$RegionImageSet, $RegionImageSet$Type} from "packages/journeymap/client/model/$RegionImageSet"
import {$DrawWayPointStep, $DrawWayPointStep$Type} from "packages/journeymap/client/render/draw/$DrawWayPointStep"
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ImageOverlay, $ImageOverlay$Type} from "packages/journeymap/client/api/display/$ImageOverlay"
import {$DrawPolygonStep, $DrawPolygonStep$Type} from "packages/journeymap/client/render/draw/$DrawPolygonStep"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MarkerOverlay, $MarkerOverlay$Type} from "packages/journeymap/client/api/display/$MarkerOverlay"
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DrawMarkerStep, $DrawMarkerStep$Type} from "packages/journeymap/client/render/draw/$DrawMarkerStep"
import {$PolygonOverlay, $PolygonOverlay$Type} from "packages/journeymap/client/api/display/$PolygonOverlay"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$DrawEntityStep, $DrawEntityStep$Type} from "packages/journeymap/client/render/draw/$DrawEntityStep"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$WorldData, $WorldData$Type} from "packages/journeymap/client/data/$WorldData"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$LoadingCache, $LoadingCache$Type} from "packages/com/google/common/cache/$LoadingCache"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DataCache extends $Enum<($DataCache)> {
static readonly "INSTANCE": $DataCache


public "stopChunkMDRetention"(): void
public static "values"(): ($DataCache)[]
public static "valueOf"(arg0: string): $DataCache
public "getAll"(arg0: long): $Map<(any), (any)>
public "purge"(): void
public "resetBlockMetadata"(): void
public "getChunkMD"(arg0: long): $ChunkMD
public "getChunkMD"(arg0: $BlockPos$Type): $ChunkMD
public "getBlockMD"(arg0: $BlockState$Type): $BlockMD
public "resetRadarCaches"(): void
public "hasBlockMD"(arg0: $BlockState$Type): boolean
public "getTriangulation"(arg0: $PolygonOverlay$Type): $List<($BlockPos)>
public "getBlockMDCount"(): integer
public "getEntityDTO"(arg0: $LivingEntity$Type): $EntityDTO
public "getLoadedBlockMDs"(): $Set<($BlockMD)>
public "getRegionImageSets"(): $LoadingCache<($RegionImageSet$Key), ($RegionImageSet)>
public "getDebugHtml"(): string
public "removeChunkMD"(arg0: $ChunkMD$Type): void
public "getRegionCoords"(): $Cache<(string), ($RegionCoord)>
public "getMessages"(arg0: boolean): $Map<(string), (any)>
public "removePlayer"(arg0: $Player$Type): void
public "getWorld"(arg0: boolean): $WorldData
public "getWaypoints"(arg0: boolean): $Collection<($Waypoint)>
public "getPlayers"(arg0: boolean): $Map<(string), ($EntityDTO)>
public "getDrawImageStep"(arg0: $ImageOverlay$Type): $DrawImageStep
public "getDrawMakerStep"(arg0: $MarkerOverlay$Type): $DrawMarkerStep
public "getDrawPolygonStep"(arg0: $PolygonOverlay$Type): $DrawPolygonStep
public "invalidatePolygon"(arg0: $PolygonOverlay$Type): void
public "getPlayer"(arg0: boolean): $EntityDTO
public static "getPlayer"(): $EntityDTO
public "getMapType"(arg0: $MapType$Name$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>): $MapType
public "getDrawWayPointStep"(arg0: $Waypoint$Type): $DrawWayPointStep
public "getMobs"(arg0: boolean): $Map<(string), ($EntityDTO)>
public "getVillagers"(arg0: boolean): $Map<(string), ($EntityDTO)>
public "getAnimals"(arg0: boolean): $Map<(string), ($EntityDTO)>
public "getDrawEntityStep"(arg0: $LivingEntity$Type): $DrawEntityStep
public "invalidateChunkMDCache"(): void
public "addChunkMD"(arg0: $ChunkMD$Type): void
get "blockMDCount"(): integer
get "loadedBlockMDs"(): $Set<($BlockMD)>
get "regionImageSets"(): $LoadingCache<($RegionImageSet$Key), ($RegionImageSet)>
get "debugHtml"(): string
get "regionCoords"(): $Cache<(string), ($RegionCoord)>
get "player"(): $EntityDTO
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataCache$Type = (("instance")) | ($DataCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataCache_ = $DataCache$Type;
}}
declare module "packages/journeymap/client/properties/$CoreProperties" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$RenderSpec$RevealShape, $RenderSpec$RevealShape$Type} from "packages/journeymap/client/task/multi/$RenderSpec$RevealShape"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$StringField, $StringField$Type} from "packages/journeymap/common/properties/config/$StringField"
import {$ClientPropertiesBase, $ClientPropertiesBase$Type} from "packages/journeymap/client/properties/$ClientPropertiesBase"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"
import {$GridSpecs, $GridSpecs$Type} from "packages/journeymap/client/model/$GridSpecs"

export class $CoreProperties extends $ClientPropertiesBase implements $Comparable<($CoreProperties)> {
static readonly "PATTERN_COLOR": string
readonly "logLevel": $StringField
readonly "autoMapPoll": $IntegerField
readonly "cacheAnimalsData": $IntegerField
readonly "cacheMobsData": $IntegerField
readonly "cachePlayerData": $IntegerField
readonly "cachePlayersData": $IntegerField
readonly "cacheVillagersData": $IntegerField
readonly "announceMod": $BooleanField
readonly "checkUpdates": $BooleanField
readonly "recordCacheStats": $BooleanField
readonly "themeName": $StringField
readonly "caveIgnoreGlass": $BooleanField
readonly "mapBathymetry": $BooleanField
readonly "mapWaterBiomeColors": $BooleanField
readonly "mapTopography": $BooleanField
readonly "mapBiome": $BooleanField
readonly "mapTransparency": $BooleanField
readonly "mapCaveLighting": $BooleanField
readonly "mapAntialiasing": $BooleanField
readonly "mapPlantShadows": $BooleanField
readonly "mapPlants": $BooleanField
readonly "mapCrops": $BooleanField
readonly "mapBlendGrass": $BooleanField
readonly "mapBlendFoliage": $BooleanField
readonly "mapBlendWater": $BooleanField
readonly "mapSurfaceAboveCaves": $BooleanField
readonly "caveBlackAsClear": $BooleanField
readonly "renderDistanceCaveMax": $IntegerField
readonly "renderDistanceSurfaceMax": $IntegerField
readonly "renderDelay": $IntegerField
readonly "revealShape": $EnumField<($RenderSpec$RevealShape)>
readonly "alwaysMapCaves": $BooleanField
readonly "alwaysMapSurface": $BooleanField
readonly "tileHighDisplayQuality": $BooleanField
readonly "maxAnimalsData": $IntegerField
readonly "maxMobsData": $IntegerField
readonly "maxPlayersData": $IntegerField
readonly "maxVillagersData": $IntegerField
readonly "hideSneakingEntities": $BooleanField
readonly "hideSpectators": $BooleanField
readonly "radarLateralDistance": $IntegerField
readonly "radarVerticalDistance": $IntegerField
readonly "tileRenderType": $IntegerField
readonly "dataCachingEnabled": $BooleanField
readonly "glErrorChecking": $BooleanField
readonly "seedId": $BooleanField
readonly "mappingEnabled": $BooleanField
readonly "optionsManagerViewed": $StringField
readonly "splashViewed": $StringField
readonly "gridSpecs": $GridSpecs
readonly "colorPassive": $StringField
readonly "colorHostile": $StringField
readonly "colorPet": $StringField
readonly "colorVillager": $StringField
readonly "colorPlayer": $StringField
readonly "colorSelf": $StringField
readonly "verboseColorPalette": $BooleanField

constructor()

public "getName"(): string
public "compareTo"(arg0: $CoreProperties$Type): integer
public "isValid"(arg0: boolean): boolean
public "updateFrom"<T extends $PropertiesBase>(arg0: T): void
public "getColor"(arg0: $StringField$Type): integer
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreProperties$Type = ($CoreProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreProperties_ = $CoreProperties$Type;
}}
declare module "packages/journeymap/client/event/handlers/$PlayerConnectHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlayerConnectHandler {

constructor()

public "onConnect"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerConnectHandler$Type = ($PlayerConnectHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerConnectHandler_ = $PlayerConnectHandler$Type;
}}
declare module "packages/journeymap/client/api/display/$IThemeToolBar" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IThemeToolBar {

 "getBottomY"(): integer
 "getY"(): integer
 "getX"(): integer
 "getWidth"(): integer
 "getHeight"(): integer
 "getMiddleY"(): integer
 "setPosition"(arg0: integer, arg1: integer): void
 "getCenterX"(): integer
 "getRightX"(): integer
 "isMouseOver"(): boolean
 "setLayoutVertical"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setReverse"(): void
 "setLayoutDistributedHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setLayoutCenteredHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setLayoutHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setLayoutCenteredVertical"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
}

export namespace $IThemeToolBar {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IThemeToolBar$Type = ($IThemeToolBar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IThemeToolBar_ = $IThemeToolBar$Type;
}}
declare module "packages/journeymap/client/data/$AllData" {
import {$AllData$Key, $AllData$Key$Type} from "packages/journeymap/client/data/$AllData$Key"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AllData extends $CacheLoader<(long), ($Map)> {

constructor()

public "load"(arg0: long): $Map<($AllData$Key), (any)>
public "getTTL"(): long
get "tTL"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AllData$Type = ($AllData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AllData_ = $AllData$Type;
}}
declare module "packages/journeymap/client/ui/component/$ScrollPaneScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Removable, $Removable$Type} from "packages/journeymap/client/ui/component/$Removable"
import {$DropDownItem, $DropDownItem$Type} from "packages/journeymap/client/ui/component/$DropDownItem"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ScrollPane, $ScrollPane$Type} from "packages/journeymap/client/ui/component/$ScrollPane"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ScrollPaneScreen extends $Screen {
 "visible": boolean
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "setParent"(arg0: $Removable$Type): void
public "display"(): void
public "setRenderSolidBackground"(arg0: boolean): void
public "setRenderDecorations"(arg0: boolean): void
public "getScrollPane"(): $ScrollPane
public "getPaneX"(): integer
public "getPaneWidth"(): integer
public "onClose"(): void
public "onClick"(arg0: $DropDownItem$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setItems"(arg0: $List$Type<($DropDownItem$Type)>): void
public "isPauseScreen"(): boolean
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "renderBackground"(arg0: $PoseStack$Type, arg1: integer): void
public "setPaneWidth"(arg0: integer): void
public "getPaneHeight"(): integer
public "setPaneY"(arg0: integer): void
public "setPaneX"(arg0: integer): void
public "mouseOverPane"(arg0: double, arg1: double): boolean
public "setPaneHeight"(arg0: integer): void
public "getPaneY"(): integer
set "parent"(value: $Removable$Type)
set "renderSolidBackground"(value: boolean)
set "renderDecorations"(value: boolean)
get "scrollPane"(): $ScrollPane
get "paneX"(): integer
get "paneWidth"(): integer
set "items"(value: $List$Type<($DropDownItem$Type)>)
get "pauseScreen"(): boolean
set "paneWidth"(value: integer)
get "paneHeight"(): integer
set "paneY"(value: integer)
set "paneX"(value: integer)
set "paneHeight"(value: integer)
get "paneY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollPaneScreen$Type = ($ScrollPaneScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollPaneScreen_ = $ScrollPaneScreen$Type;
}}
declare module "packages/journeymap/client/api/impl/$InfoSlotFactory" {
import {$RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar, $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar$Type} from "packages/journeymap/client/api/event/$RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $InfoSlotFactory implements $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar {

constructor()

public "register"(arg0: string, arg1: string, arg2: long, arg3: $Supplier$Type<(string)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfoSlotFactory$Type = ($InfoSlotFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfoSlotFactory_ = $InfoSlotFactory$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$WaypointManagerItem$Sort" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$WaypointManagerItem, $WaypointManagerItem$Type} from "packages/journeymap/client/ui/waypoint/$WaypointManagerItem"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"

export class $WaypointManagerItem$Sort implements $Comparator<($WaypointManagerItem)> {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compare"(arg0: $WaypointManagerItem$Type, arg1: $WaypointManagerItem$Type): integer
public static "reverseOrder"<T extends $Comparable<(any)>>(): $Comparator<($WaypointManagerItem)>
public static "comparing"<T, U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($WaypointManagerItem)>
public static "comparing"<T, U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($WaypointManagerItem)>
public "thenComparing"(arg0: $Comparator$Type<(any)>): $Comparator<($WaypointManagerItem)>
public "thenComparing"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($WaypointManagerItem)>
public "thenComparing"<U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($WaypointManagerItem)>
public static "comparingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Comparator<($WaypointManagerItem)>
public static "comparingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Comparator<($WaypointManagerItem)>
public static "comparingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($WaypointManagerItem)>
public "reversed"(): $Comparator<($WaypointManagerItem)>
public "thenComparingInt"(arg0: $ToIntFunction$Type<(any)>): $Comparator<($WaypointManagerItem)>
public "thenComparingLong"(arg0: $ToLongFunction$Type<(any)>): $Comparator<($WaypointManagerItem)>
public "thenComparingDouble"(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($WaypointManagerItem)>
public static "naturalOrder"<T extends $Comparable<(any)>>(): $Comparator<($WaypointManagerItem)>
public static "nullsFirst"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($WaypointManagerItem)>
public static "nullsLast"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($WaypointManagerItem)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointManagerItem$Sort$Type = ($WaypointManagerItem$Sort);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointManagerItem$Sort_ = $WaypointManagerItem$Sort$Type;
}}
declare module "packages/journeymap/client/api/model/$ShapeProperties" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ShapeProperties {

constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "setImage"(arg0: $NativeImage$Type): $ShapeProperties
public "getImageLocation"(): $ResourceLocation
public "setFillOpacity"(arg0: float): $ShapeProperties
public "setFillColor"(arg0: integer): $ShapeProperties
public "getImage"(): $NativeImage
public "setStrokeColor"(arg0: integer): $ShapeProperties
public "setStrokeOpacity"(arg0: float): $ShapeProperties
public "setStrokeWidth"(arg0: float): $ShapeProperties
public "getStrokeWidth"(): float
public "getStrokeColor"(): integer
public "getFillOpacity"(): float
public "getFillColor"(): integer
public "getStrokeOpacity"(): float
public "getTexturePositionX"(): double
public "setTexturePositionX"(arg0: double): $ShapeProperties
public "getTexturePositionY"(): double
public "setTexturePositionY"(arg0: double): $ShapeProperties
public "getTextureScaleY"(): double
public "setTextureScaleX"(arg0: double): $ShapeProperties
public "setTextureScaleY"(arg0: double): $ShapeProperties
public "getTextureScaleX"(): double
public "setImageLocation"(arg0: $ResourceLocation$Type): $ShapeProperties
set "image"(value: $NativeImage$Type)
get "imageLocation"(): $ResourceLocation
set "fillOpacity"(value: float)
set "fillColor"(value: integer)
get "image"(): $NativeImage
set "strokeColor"(value: integer)
set "strokeOpacity"(value: float)
set "strokeWidth"(value: float)
get "strokeWidth"(): float
get "strokeColor"(): integer
get "fillOpacity"(): float
get "fillColor"(): integer
get "strokeOpacity"(): float
get "texturePositionX"(): double
set "texturePositionX"(value: double)
get "texturePositionY"(): double
set "texturePositionY"(value: double)
get "textureScaleY"(): double
set "textureScaleX"(value: double)
set "textureScaleY"(value: double)
get "textureScaleX"(): double
set "imageLocation"(value: $ResourceLocation$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapeProperties$Type = ($ShapeProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapeProperties_ = $ShapeProperties$Type;
}}
declare module "packages/journeymap/client/event/handlers/$HudOverlayHandler" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $HudOverlayHandler {

constructor()

public "preOverlay"(arg0: $GuiGraphics$Type): boolean
public "postOverlay"(arg0: $GuiGraphics$Type): void
public "onRenderOverlayDebug"(arg0: $List$Type<(string)>): void
public "onRenderOverlay"(arg0: $GuiGraphics$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HudOverlayHandler$Type = ($HudOverlayHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HudOverlayHandler_ = $HudOverlayHandler$Type;
}}
declare module "packages/journeymap/client/api/event/$WaypointEvent" {
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/api/display/$Waypoint"
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$WaypointEvent$Context, $WaypointEvent$Context$Type} from "packages/journeymap/client/api/event/$WaypointEvent$Context"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"

export class $WaypointEvent extends $ClientEvent {
readonly "waypoint": $Waypoint
readonly "context": $WaypointEvent$Context
readonly "type": $ClientEvent$Type
readonly "dimension": $ResourceKey<($Level)>
readonly "timestamp": long


public "getContext"(): $WaypointEvent$Context
public "getWaypoint"(): $Waypoint
get "context"(): $WaypointEvent$Context
get "waypoint"(): $Waypoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointEvent$Type = ($WaypointEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointEvent_ = $WaypointEvent$Type;
}}
declare module "packages/journeymap/client/api/display/$DisplayType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $DisplayType extends $Enum<($DisplayType)> {
static readonly "Image": $DisplayType
static readonly "Marker": $DisplayType
static readonly "Polygon": $DisplayType
static readonly "Waypoint": $DisplayType
static readonly "WaypointGroup": $DisplayType


public static "values"(): ($DisplayType)[]
public static "valueOf"(arg0: string): $DisplayType
public static "of"(arg0: $Class$Type<(any)>): $DisplayType
public "getImplClass"(): $Class<(any)>
get "implClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayType$Type = (("waypoint") | ("image") | ("polygon") | ("marker") | ("waypointgroup")) | ($DisplayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayType_ = $DisplayType$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/$MapChat" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$ChatScreen, $ChatScreen$Type} from "packages/net/minecraft/client/gui/screens/$ChatScreen"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $MapChat extends $ChatScreen {
static readonly "MOUSE_SCROLL_SPEED": double
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: string, arg1: boolean)

public "isHidden"(): boolean
public "close"(): void
public "setText"(arg0: string): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "removed"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "tick"(): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "setHidden"(arg0: boolean): void
get "hidden"(): boolean
set "text"(value: string)
set "hidden"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapChat$Type = ($MapChat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapChat_ = $MapChat$Type;
}}
declare module "packages/journeymap/client/event/dispatchers/$EventDispatcher" {
import {$PopupMenuEvent$WaypointPopupMenuEvent, $PopupMenuEvent$WaypointPopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$WaypointPopupMenuEvent"
import {$FullscreenDisplayEvent$AddonButtonDisplayEvent, $FullscreenDisplayEvent$AddonButtonDisplayEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$AddonButtonDisplayEvent"
import {$PopupMenuEvent$FullscreenPopupMenuEvent, $PopupMenuEvent$FullscreenPopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$FullscreenPopupMenuEvent"
import {$FullscreenDisplayEvent$CustomToolbarEvent, $FullscreenDisplayEvent$CustomToolbarEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$CustomToolbarEvent"
import {$FullscreenDisplayEvent$MapTypeButtonDisplayEvent, $FullscreenDisplayEvent$MapTypeButtonDisplayEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$MapTypeButtonDisplayEvent"
import {$EntityRadarUpdateEvent, $EntityRadarUpdateEvent$Type} from "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent"

export interface $EventDispatcher {

 "popupMenuEvent"(arg0: $PopupMenuEvent$FullscreenPopupMenuEvent$Type): void
 "getMapTypeToolbar"(arg0: $FullscreenDisplayEvent$MapTypeButtonDisplayEvent$Type): void
 "getCustomToolBars"(arg0: $FullscreenDisplayEvent$CustomToolbarEvent$Type): void
 "getAddonToolbar"(arg0: $FullscreenDisplayEvent$AddonButtonDisplayEvent$Type): void
 "popupWaypointMenuEvent"(arg0: $PopupMenuEvent$WaypointPopupMenuEvent$Type): void
 "entityRadarUpdateEvent"(arg0: $EntityRadarUpdateEvent$Type): void
}

export namespace $EventDispatcher {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventDispatcher$Type = ($EventDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventDispatcher_ = $EventDispatcher$Type;
}}
declare module "packages/journeymap/client/cartography/$Strata" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"

export class $Strata {

constructor(arg0: string, arg1: integer, arg2: integer, arg3: boolean)

public "toString"(): string
public "isEmpty"(): boolean
public "reset"(): void
public "release"(arg0: $Stratum$Type): void
public "nextUp"(arg0: $IChunkRenderer$Type, arg1: boolean): $Stratum
public "push"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer): $Stratum
public "push"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: integer, arg3: integer, arg4: integer): $Stratum
public "getTopY"(): integer
public "getBottomY"(): integer
public "setRenderCaveColor"(arg0: integer): void
public "getRenderCaveColor"(): integer
public "setBlocksFound"(arg0: boolean): void
public "setRenderDayColor"(arg0: integer): void
public "getRenderDayColor"(): integer
public "setFluidColor"(arg0: integer): void
public "isMapCaveLighting"(): boolean
public "setTopFluidY"(arg0: integer): void
public "getTopFluidY"(): integer
public "setBottomY"(arg0: integer): void
public "setBottomFluidY"(arg0: integer): void
public "setMaxLightLevel"(arg0: integer): void
public "getMaxLightLevel"(): integer
public "getBottomFluidY"(): integer
public "isBlocksFound"(): boolean
public "setTopY"(arg0: integer): void
public "isUnderground"(): boolean
public "getFluidColor"(): integer
public "setLightAttenuation"(arg0: integer): void
public "getLightAttenuation"(): integer
public "setRenderNightColor"(arg0: integer): void
public "getRenderNightColor"(): integer
get "empty"(): boolean
get "topY"(): integer
get "bottomY"(): integer
set "renderCaveColor"(value: integer)
get "renderCaveColor"(): integer
set "blocksFound"(value: boolean)
set "renderDayColor"(value: integer)
get "renderDayColor"(): integer
set "fluidColor"(value: integer)
get "mapCaveLighting"(): boolean
set "topFluidY"(value: integer)
get "topFluidY"(): integer
set "bottomY"(value: integer)
set "bottomFluidY"(value: integer)
set "maxLightLevel"(value: integer)
get "maxLightLevel"(): integer
get "bottomFluidY"(): integer
get "blocksFound"(): boolean
set "topY"(value: integer)
get "underground"(): boolean
get "fluidColor"(): integer
set "lightAttenuation"(value: integer)
get "lightAttenuation"(): integer
set "renderNightColor"(value: integer)
get "renderNightColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Strata$Type = ($Strata);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Strata_ = $Strata$Type;
}}
declare module "packages/journeymap/client/data/$PlayersData" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlayersData extends $CacheLoader<($Class), ($Map<(string), ($EntityDTO)>)> {

constructor()

public "load"(arg0: $Class$Type<(any)>): $Map<(string), ($EntityDTO)>
public "getTTL"(): long
get "tTL"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayersData$Type = ($PlayersData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayersData_ = $PlayersData$Type;
}}
declare module "packages/journeymap/client/api/option/$CustomTextOption" {
import {$CustomOption, $CustomOption$Type} from "packages/journeymap/client/api/option/$CustomOption"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $CustomTextOption extends $CustomOption<(string)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomTextOption$Type = ($CustomTextOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomTextOption_ = $CustomTextOption$Type;
}}
declare module "packages/journeymap/client/api/display/$WaypointGroup" {
import {$IWaypointDisplay, $IWaypointDisplay$Type} from "packages/journeymap/client/api/display/$IWaypointDisplay"
import {$WaypointBase, $WaypointBase$Type} from "packages/journeymap/client/api/model/$WaypointBase"

export class $WaypointGroup extends $WaypointBase<($WaypointGroup)> {
static readonly "VERSION": double

constructor(arg0: string, arg1: string)
constructor(arg0: string, arg1: string, arg2: string)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getDisplayOrder"(): integer
public "setDisplayOrder"(arg0: integer): $WaypointGroup
public "setDefaultDisplay"(arg0: $IWaypointDisplay$Type): $WaypointGroup
get "displayOrder"(): integer
set "displayOrder"(value: integer)
set "defaultDisplay"(value: $IWaypointDisplay$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointGroup$Type = ($WaypointGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointGroup_ = $WaypointGroup$Type;
}}
declare module "packages/journeymap/client/mod/vanilla/$VanillaBlockHandler" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"

export class $VanillaBlockHandler implements $IModBlockHandler {

constructor()

public "postInitialize"(arg0: $BlockMD$Type): void
public "initialize"(arg0: $BlockMD$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaBlockHandler$Type = ($VanillaBlockHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaBlockHandler_ = $VanillaBlockHandler$Type;
}}
declare module "packages/journeymap/client/io/nbt/$JMChunkLoader" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$ChunkStorage, $ChunkStorage$Type} from "packages/net/minecraft/world/level/chunk/storage/$ChunkStorage"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $JMChunkLoader {

constructor()

public static "getChunkMdFromMemory"(arg0: $Level$Type, arg1: integer, arg2: integer): $ChunkMD
public static "getChunkMD"(arg0: $ChunkStorage$Type, arg1: $Minecraft$Type, arg2: $ChunkPos$Type, arg3: boolean): $ChunkMD
public static "getChunkFromNBT"(arg0: $ServerLevel$Type, arg1: $ChunkPos$Type, arg2: $CompoundTag$Type, arg3: boolean): $ChunkMD
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMChunkLoader$Type = ($JMChunkLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMChunkLoader_ = $JMChunkLoader$Type;
}}
declare module "packages/journeymap/client/api/impl/$ThemeFactory" {
import {$ThemeButton, $ThemeButton$Type} from "packages/journeymap/client/ui/theme/$ThemeButton"
import {$IThemeButton$Action, $IThemeButton$Action$Type} from "packages/journeymap/client/api/display/$IThemeButton$Action"

export class $ThemeFactory {


public "getThemeButton"(arg0: string, arg1: string, arg2: $IThemeButton$Action$Type): $ThemeButton
public "getThemeButton"(arg0: string, arg1: string, arg2: string, arg3: $IThemeButton$Action$Type): $ThemeButton
public "getThemeToggleButton"(arg0: string, arg1: string, arg2: $IThemeButton$Action$Type): $ThemeButton
public "getThemeToggleButton"(arg0: string, arg1: string, arg2: string, arg3: $IThemeButton$Action$Type): $ThemeButton
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeFactory$Type = ($ThemeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeFactory_ = $ThemeFactory$Type;
}}
declare module "packages/journeymap/client/api/display/$Context$MapType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Context, $Context$Type} from "packages/journeymap/client/api/display/$Context"

export class $Context$MapType extends $Enum<($Context$MapType)> implements $Context {
static readonly "Any": $Context$MapType
static readonly "Day": $Context$MapType
static readonly "Night": $Context$MapType
static readonly "Underground": $Context$MapType
static readonly "Topo": $Context$MapType
static readonly "Biome": $Context$MapType


public static "values"(): ($Context$MapType)[]
public static "valueOf"(arg0: string): $Context$MapType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Context$MapType$Type = (("underground") | ("biome") | ("night") | ("topo") | ("any") | ("day")) | ($Context$MapType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Context$MapType_ = $Context$MapType$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeWorldEvent" {
import {$LevelEvent$Unload, $LevelEvent$Unload$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Unload"
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"

export class $ForgeWorldEvent implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onUnload"(arg0: $LevelEvent$Unload$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeWorldEvent$Type = ($ForgeWorldEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeWorldEvent_ = $ForgeWorldEvent$Type;
}}
declare module "packages/journeymap/client/event/dispatchers/$FullscreenEventDispatcher" {
import {$IBlockInfo, $IBlockInfo$Type} from "packages/journeymap/client/api/model/$IBlockInfo"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FullscreenEventDispatcher {

constructor()

public static "moveEvent"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $IBlockInfo$Type, arg2: $Point2D$Double$Type): void
public static "clickEventPre"(arg0: $BlockPos$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Point2D$Double$Type, arg3: integer): boolean
public static "dragEventPre"(arg0: $BlockPos$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Point2D$Double$Type, arg3: integer): boolean
public static "dragEventPost"(arg0: $BlockPos$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Point2D$Double$Type, arg3: integer): void
public static "clickEventPost"(arg0: $BlockPos$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Point2D$Double$Type, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenEventDispatcher$Type = ($FullscreenEventDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenEventDispatcher_ = $FullscreenEventDispatcher$Type;
}}
declare module "packages/journeymap/client/ui/component/$ScrollListPane" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$AbstractSelectionList, $AbstractSelectionList$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$Slot, $Slot$Type} from "packages/journeymap/client/ui/component/$Slot"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $ScrollListPane<T extends $Slot> extends $AbstractSelectionList<(any)> {
 "lastTooltipMetadata": $SlotMetadata<(any)>
 "lastTooltip": $List<($FormattedCharSequence)>
 "lastTooltipTime": long
 "hoverDelay": long
 "scrolling": boolean
 "hovered": E

constructor(arg0: $JmUI$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public "getSlot"(arg0: integer): $Slot
public "getLastPressed"(): $SlotMetadata<(any)>
public "setAlignTop"(arg0: boolean): void
public "getRootSlots"(): $List<(T)>
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "updateNarration"(arg0: $NarrationElementOutput$Type): void
public "getTop"(): integer
public "getBottom"(): integer
public "getHeight"(): integer
public "updateSlots"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "m_7987_"(arg0: integer): boolean
public "m_239227_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setHover"(arg0: boolean): void
public "resetLastPressed"(): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "scrollTo"(arg0: $Slot$Type): void
public "getLastPressedParentSlot"(): $Slot
public "setSlots"(arg0: $List$Type<(T)>): void
public "getRowWidth"(): integer
public "updateSize"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
get "lastPressed"(): $SlotMetadata<(any)>
set "alignTop"(value: boolean)
get "rootSlots"(): $List<(T)>
get "top"(): integer
get "bottom"(): integer
get "height"(): integer
set "hover"(value: boolean)
get "lastPressedParentSlot"(): $Slot
set "slots"(value: $List$Type<(T)>)
get "rowWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollListPane$Type<T> = ($ScrollListPane<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollListPane_<T> = $ScrollListPane$Type<(T)>;
}}
declare module "packages/journeymap/client/ui/minimap/$LabelVars" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LabelVars {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LabelVars$Type = ($LabelVars);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LabelVars_ = $LabelVars$Type;
}}
declare module "packages/journeymap/client/api/option/$OptionsRegistry" {
import {$Option, $Option$Type} from "packages/journeymap/client/api/option/$Option"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $OptionsRegistry {
static readonly "OPTION_REGISTRY": $Map<(string), ($Map<(string), ($Option<(any)>)>)>

constructor()

/**
 * 
 * @deprecated
 */
public static "register"(arg0: string, arg1: $Option$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsRegistry$Type = ($OptionsRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsRegistry_ = $OptionsRegistry$Type;
}}
declare module "packages/journeymap/client/event/dispatchers/forge/$ForgeEventDispatcher" {
import {$PopupMenuEvent$WaypointPopupMenuEvent, $PopupMenuEvent$WaypointPopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$WaypointPopupMenuEvent"
import {$FullscreenDisplayEvent$AddonButtonDisplayEvent, $FullscreenDisplayEvent$AddonButtonDisplayEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$AddonButtonDisplayEvent"
import {$EventDispatcher, $EventDispatcher$Type} from "packages/journeymap/client/event/dispatchers/$EventDispatcher"
import {$PopupMenuEvent$FullscreenPopupMenuEvent, $PopupMenuEvent$FullscreenPopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$FullscreenPopupMenuEvent"
import {$FullscreenDisplayEvent$CustomToolbarEvent, $FullscreenDisplayEvent$CustomToolbarEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$CustomToolbarEvent"
import {$FullscreenDisplayEvent$MapTypeButtonDisplayEvent, $FullscreenDisplayEvent$MapTypeButtonDisplayEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$MapTypeButtonDisplayEvent"
import {$EntityRadarUpdateEvent, $EntityRadarUpdateEvent$Type} from "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent"

export class $ForgeEventDispatcher implements $EventDispatcher {

constructor()

public "popupMenuEvent"(arg0: $PopupMenuEvent$FullscreenPopupMenuEvent$Type): void
public "getMapTypeToolbar"(arg0: $FullscreenDisplayEvent$MapTypeButtonDisplayEvent$Type): void
public "getCustomToolBars"(arg0: $FullscreenDisplayEvent$CustomToolbarEvent$Type): void
public "getAddonToolbar"(arg0: $FullscreenDisplayEvent$AddonButtonDisplayEvent$Type): void
public "popupWaypointMenuEvent"(arg0: $PopupMenuEvent$WaypointPopupMenuEvent$Type): void
public "entityRadarUpdateEvent"(arg0: $EntityRadarUpdateEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventDispatcher$Type = ($ForgeEventDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventDispatcher_ = $ForgeEventDispatcher$Type;
}}
declare module "packages/journeymap/client/ui/option/$DateFormat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DateFormat {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateFormat$Type = ($DateFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateFormat_ = $DateFormat$Type;
}}
declare module "packages/journeymap/client/ui/option/$TimeFormat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TimeFormat {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeFormat$Type = ($TimeFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeFormat_ = $TimeFormat$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeCompassPoints" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MiniMapProperties, $MiniMapProperties$Type} from "packages/journeymap/client/properties/$MiniMapProperties"
import {$Theme$Minimap$MinimapSpec, $Theme$Minimap$MinimapSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSpec"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $ThemeCompassPoints {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Theme$Minimap$MinimapSpec$Type, arg5: $MiniMapProperties$Type, arg6: $Texture$Type, arg7: float)

public "drawLabels"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: double): void
public "setPosition"(arg0: double, arg1: double): void
public static "getCompassPointScale"(arg0: float, arg1: $Theme$Minimap$MinimapSpec$Type, arg2: $Texture$Type): float
public "drawPoints"(arg0: $PoseStack$Type, arg1: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeCompassPoints$Type = ($ThemeCompassPoints);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeCompassPoints_ = $ThemeCompassPoints$Type;
}}
declare module "packages/journeymap/client/ui/option/$LocationFormat" {
import {$LocationFormat$LocationFormatKeys, $LocationFormat$LocationFormatKeys$Type} from "packages/journeymap/client/ui/option/$LocationFormat$LocationFormatKeys"

export class $LocationFormat {

constructor()

public "getFormatKeys"(arg0: string): $LocationFormat$LocationFormatKeys
public "getLabel"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocationFormat$Type = ($LocationFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocationFormat_ = $LocationFormat$Type;
}}
declare module "packages/journeymap/client/api/model/$MapPolygonWithHoles" {
import {$MapPolygon, $MapPolygon$Type} from "packages/journeymap/client/api/model/$MapPolygon"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MapPolygonWithHoles {
readonly "hull": $MapPolygon
readonly "holes": $List<($MapPolygon)>

constructor(arg0: $MapPolygon$Type, arg1: $List$Type<($MapPolygon$Type)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapPolygonWithHoles$Type = ($MapPolygonWithHoles);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapPolygonWithHoles_ = $MapPolygonWithHoles$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeButton" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$BooleanPropertyButton, $BooleanPropertyButton$Type} from "packages/journeymap/client/ui/component/$BooleanPropertyButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IThemeButton, $IThemeButton$Type} from "packages/journeymap/client/api/display/$IThemeButton"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$Theme$Control$ButtonSpec, $Theme$Control$ButtonSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Control$ButtonSpec"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ThemeButton extends $BooleanPropertyButton implements $IThemeButton {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Theme$Type, arg1: string, arg2: string, arg3: boolean, arg4: string, arg5: $Button$OnPress$Type)
constructor(arg0: $Theme$Type, arg1: string, arg2: string, arg3: $Button$OnPress$Type)
constructor(arg0: $Theme$Type, arg1: string, arg2: string, arg3: string, arg4: $BooleanField$Type, arg5: $Button$OnPress$Type)

public "getFormattedTooltip"(): $List<($FormattedCharSequence)>
public "getButton"(): $Button
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setStaysOn"(arg0: boolean): void
public "getButtonSpec"(): $Theme$Control$ButtonSpec
public "updateTheme"(arg0: $Theme$Type): void
public "isStaysOn"(): boolean
public "drawNativeButton"(arg0: $PoseStack$Type, arg1: integer, arg2: integer): void
public "hasValidTextures"(): boolean
public "setAdditionalTooltips"(arg0: $List$Type<($FormattedCharSequence$Type)>): void
public "setDisplayClickToggle"(arg0: boolean): void
public "setEnabled"(arg0: boolean): void
public "isActive"(): boolean
public "setTooltip"(...arg0: (string)[]): void
public "setLabels"(arg0: string, arg1: string): void
public "setDrawButton"(arg0: boolean): void
public "setToggled"(arg0: boolean): void
public "getToggled"(): boolean
public "toggle"(): void
get "formattedTooltip"(): $List<($FormattedCharSequence)>
get "button"(): $Button
set "staysOn"(value: boolean)
get "buttonSpec"(): $Theme$Control$ButtonSpec
get "staysOn"(): boolean
set "additionalTooltips"(value: $List$Type<($FormattedCharSequence$Type)>)
set "displayClickToggle"(value: boolean)
set "enabled"(value: boolean)
get "active"(): boolean
set "tooltip"(value: (string)[])
set "drawButton"(value: boolean)
set "toggled"(value: boolean)
get "toggled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeButton$Type = ($ThemeButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeButton_ = $ThemeButton$Type;
}}
declare module "packages/journeymap/client/cartography/$Stratum" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $Stratum {


public "isFluid"(): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clear"(): void
public "getBlockMD"(): $BlockMD
public "getLightOpacity"(): integer
public "getY"(): integer
public "setX"(arg0: integer): void
public "getZ"(): integer
public "setY"(arg0: integer): void
public "setZ"(arg0: integer): void
public "getX"(): integer
public "isUninitialized"(): boolean
public "getWorldHasNoSky"(): boolean
public "getNightColor"(): integer
public "setDayColor"(arg0: integer): void
public "setNightColor"(arg0: integer): void
public "setCaveColor"(arg0: integer): void
public "getChunkMd"(): $ChunkMD
public "getDayColor"(): integer
public "getCaveColor"(): integer
public "setBlockMD"(arg0: $BlockMD$Type): void
public "setLightOpacity"(arg0: integer): void
public "setChunkMd"(arg0: $ChunkMD$Type): void
public "getLightLevel"(): integer
public "getBlockPos"(): $BlockPos
public "setFluid"(arg0: boolean): void
public "setLightLevel"(arg0: integer): void
public "getWorldAmbientLight"(): float
get "fluid"(): boolean
get "blockMD"(): $BlockMD
get "lightOpacity"(): integer
get "y"(): integer
set "x"(value: integer)
get "z"(): integer
set "y"(value: integer)
set "z"(value: integer)
get "x"(): integer
get "uninitialized"(): boolean
get "worldHasNoSky"(): boolean
get "nightColor"(): integer
set "dayColor"(value: integer)
set "nightColor"(value: integer)
set "caveColor"(value: integer)
get "chunkMd"(): $ChunkMD
get "dayColor"(): integer
get "caveColor"(): integer
set "blockMD"(value: $BlockMD$Type)
set "lightOpacity"(value: integer)
set "chunkMd"(value: $ChunkMD$Type)
get "lightLevel"(): integer
get "blockPos"(): $BlockPos
set "fluid"(value: boolean)
set "lightLevel"(value: integer)
get "worldAmbientLight"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Stratum$Type = ($Stratum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Stratum_ = $Stratum$Type;
}}
declare module "packages/journeymap/client/api/util/$UIState" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"

export class $UIState {
readonly "ui": $Context$UI
readonly "active": boolean
readonly "dimension": $ResourceKey<($Level)>
readonly "zoom": integer
readonly "mapType": $Context$MapType
readonly "mapCenter": $BlockPos
readonly "chunkY": integer
readonly "blockBounds": $AABB
readonly "displayBounds": $Rectangle2D$Double
readonly "blockSize": double

constructor(arg0: $Context$UI$Type, arg1: boolean, arg2: $ResourceKey$Type<($Level$Type)>, arg3: integer, arg4: $Context$MapType$Type, arg5: $BlockPos$Type, arg6: integer, arg7: $AABB$Type, arg8: $Rectangle2D$Double$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "newInactive"(arg0: $Context$UI$Type, arg1: $Minecraft$Type): $UIState
public static "newInactive"(arg0: $UIState$Type): $UIState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UIState$Type = ($UIState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UIState_ = $UIState$Type;
}}
declare module "packages/journeymap/client/event/handlers/keymapping/$KeyConflictContext" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IKeyConflictContext, $IKeyConflictContext$Type} from "packages/net/minecraftforge/client/settings/$IKeyConflictContext"

export class $KeyConflictContext extends $Enum<($KeyConflictContext)> {
static readonly "UNIVERSAL": $KeyConflictContext
static readonly "GUI": $KeyConflictContext
static readonly "IN_GAME": $KeyConflictContext


public static "values"(): ($KeyConflictContext)[]
public static "valueOf"(arg0: string): $KeyConflictContext
public "isActive"(): boolean
public "getForge"(): $IKeyConflictContext
get "active"(): boolean
get "forge"(): $IKeyConflictContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyConflictContext$Type = (("in_game") | ("gui") | ("universal")) | ($KeyConflictContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyConflictContext_ = $KeyConflictContext$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$PopupMenuEvent$Layer" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PopupMenuEvent$Layer extends $Enum<($PopupMenuEvent$Layer)> {
static readonly "WAYPOINT": $PopupMenuEvent$Layer
static readonly "FULLSCREEN": $PopupMenuEvent$Layer


public static "values"(): ($PopupMenuEvent$Layer)[]
public static "valueOf"(arg0: string): $PopupMenuEvent$Layer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuEvent$Layer$Type = (("waypoint") | ("fullscreen")) | ($PopupMenuEvent$Layer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuEvent$Layer_ = $PopupMenuEvent$Layer$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapCircle" {
import {$Theme$ImageSpec, $Theme$ImageSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ImageSpec"
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"
import {$Theme$LabelSpec, $Theme$LabelSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$LabelSpec"
import {$Theme$Minimap$MinimapSpec, $Theme$Minimap$MinimapSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSpec"

export class $Theme$Minimap$MinimapCircle extends $Theme$Minimap$MinimapSpec {
 "rim256": $Theme$ImageSpec
 "mask256": $Theme$ImageSpec
 "rim512": $Theme$ImageSpec
 "mask512": $Theme$ImageSpec
 "rotates": boolean
 "margin": integer
 "labelTop": $Theme$LabelSpec
 "labelTopInside": boolean
 "labelBottom": $Theme$LabelSpec
 "labelBottomInside": boolean
 "compassLabel": $Theme$LabelSpec
 "compassPoint": $Theme$ImageSpec
 "compassPointLabelPad": integer
 "compassPointOffset": double
 "compassShowNorth": boolean
 "compassShowSouth": boolean
 "compassShowEast": boolean
 "compassShowWest": boolean
 "waypointOffset": double
 "reticle": $Theme$ColorSpec
 "reticleHeading": $Theme$ColorSpec
 "reticleThickness": double
 "reticleHeadingThickness": double
 "reticleOffsetOuter": integer
 "reticleOffsetInner": integer
 "frame": $Theme$ColorSpec
 "prefix": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Minimap$MinimapCircle$Type = ($Theme$Minimap$MinimapCircle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Minimap$MinimapCircle_ = $Theme$Minimap$MinimapCircle$Type;
}}
declare module "packages/journeymap/client/render/draw/$WaypointDrawStepFactory" {
import {$DrawWayPointStep, $DrawWayPointStep$Type} from "packages/journeymap/client/render/draw/$DrawWayPointStep"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointDrawStepFactory {

constructor()

public "prepareSteps"(arg0: $Collection$Type<($Waypoint$Type)>, arg1: $GridRenderer$Type, arg2: boolean, arg3: boolean): $List<($DrawWayPointStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointDrawStepFactory$Type = ($WaypointDrawStepFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointDrawStepFactory_ = $WaypointDrawStepFactory$Type;
}}
declare module "packages/journeymap/client/api/impl/$BlockInfo$Builder" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockInfo, $BlockInfo$Type} from "packages/journeymap/client/api/impl/$BlockInfo"

export class $BlockInfo$Builder {

constructor()

public "withBlockPos"(arg0: $BlockPos$Type): $BlockInfo$Builder
public "withChunk"(arg0: $LevelChunk$Type): $BlockInfo$Builder
public "withBlockState"(arg0: $BlockState$Type): $BlockInfo$Builder
public "withBiome"(arg0: $Biome$Type): $BlockInfo$Builder
public "withChunkPos"(arg0: $ChunkPos$Type): $BlockInfo$Builder
public "withRegionZ"(arg0: integer): $BlockInfo$Builder
public "withBlock"(arg0: $Block$Type): $BlockInfo$Builder
public "withRegionX"(arg0: integer): $BlockInfo$Builder
public "build"(): $BlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInfo$Builder$Type = ($BlockInfo$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInfo$Builder_ = $BlockInfo$Builder$Type;
}}
declare module "packages/journeymap/client/ui/component/$JmUI" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ScreenAccess, $ScreenAccess$Type} from "packages/journeymap/common/accessors/$ScreenAccess"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"

export class $JmUI extends $Screen implements $ScreenAccess {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: string)
constructor(arg0: string, arg1: $Screen$Type)

public "close"(): void
public "init"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): void
public "getRenderables"(): $List<($Renderable)>
public "renderTooltip"(arg0: $GuiGraphics$Type, arg1: (string)[], arg2: integer, arg3: integer): void
public "getMinecraft"(): $Minecraft
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "m_7856_"(): void
public "renderWrappedToolTip"(arg0: $GuiGraphics$Type, arg1: $List$Type<(any)>, arg2: integer, arg3: integer, arg4: $Font$Type): void
public "getFontRenderer"(): $Font
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getReturnDisplay"(): $Screen
public "setRenderBottomBar"(arg0: boolean): void
public "isPauseScreen"(): boolean
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "sizeDisplay"(arg0: $PoseStack$Type, arg1: boolean): void
public "getButtonList"(): $List<(any)>
public "getScaleFactor"(): double
public "drawGradientRect"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
get "renderables"(): $List<($Renderable)>
get "minecraft"(): $Minecraft
get "fontRenderer"(): $Font
get "returnDisplay"(): $Screen
set "renderBottomBar"(value: boolean)
get "pauseScreen"(): boolean
get "buttonList"(): $List<(any)>
get "scaleFactor"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JmUI$Type = ($JmUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JmUI_ = $JmUI$Type;
}}
declare module "packages/journeymap/client/ui/$GuiUtils" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GuiUtils {

constructor()

public static "drawTexturedModalRect"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: float): void
public static "drawContinuousTexturedBox"(arg0: $PoseStack$Type, arg1: $ResourceLocation$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer, arg12: integer, arg13: integer, arg14: float): void
public static "drawContinuousTexturedBox"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer, arg12: integer, arg13: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiUtils$Type = ($GuiUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiUtils_ = $GuiUtils$Type;
}}
declare module "packages/journeymap/client/model/$BlockMD" {
import {$IBlockSpritesProxy, $IBlockSpritesProxy$Type} from "packages/journeymap/client/mod/$IBlockSpritesProxy"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$BlockFlag, $BlockFlag$Type} from "packages/journeymap/client/model/$BlockFlag"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockMD implements $Comparable<($BlockMD)> {
static readonly "FlagsPlantAndCrop": $EnumSet<($BlockFlag)>
static readonly "FlagsNormal": $EnumSet<($BlockFlag)>
static readonly "AIRBLOCK": $BlockMD
static readonly "VOIDBLOCK": $BlockMD


public "isGrass"(): boolean
public "hasAnyFlag"(arg0: $EnumSet$Type<($BlockFlag$Type)>): boolean
public static "setAllFlags"(arg0: $Block$Type, ...arg1: ($BlockFlag$Type)[]): void
public static "getBlockStateId"(arg0: $BlockMD$Type): string
public static "getBlockStateId"(arg0: $BlockState$Type): string
public "getBlockStateId"(): string
public "isFoliage"(): boolean
public "isFluid"(): boolean
public "getValidStateMDs"(): $Set<($BlockMD)>
public static "getBlockId"(arg0: $BlockState$Type): string
public static "getBlockId"(arg0: $BlockMD$Type): string
public "getBlockId"(): string
public "hasNoShadow"(): boolean
public "hasTransparency"(): boolean
public "getBlockColorProxy"(): $IBlockColorProxy
public "setBlockColorProxy"(arg0: $IBlockColorProxy$Type): void
public "getBlockDomain"(): string
public "isVanillaBlock"(): boolean
public "getName"(): string
public static "get"(arg0: $BlockState$Type): $BlockMD
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $BlockMD$Type): integer
public static "reset"(): void
public static "getAll"(): $Set<($BlockMD)>
public "setColor"(arg0: integer): integer
public "getFlags"(): $EnumSet<($BlockFlag)>
public "getBlock"(): $Block
public static "getBlockMD"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): $BlockMD
public "isIgnore"(): boolean
public static "getAllMinecraft"(): $Set<($BlockMD)>
public "getTextureColor"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): integer
public "getTextureColor"(): integer
public "clearColor"(): void
public "isWater"(): boolean
public "hasFlag"(arg0: $BlockFlag$Type): boolean
public "addFlags"(...arg0: ($BlockFlag$Type)[]): void
public "addFlags"(arg0: $Collection$Type<($BlockFlag$Type)>): void
public static "getAllValid"(): $Set<($BlockMD)>
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): integer
public static "getBlockName"(arg0: $Block$Type): string
public "getAlpha"(): float
public "setAlpha"(arg0: float): void
public "getBlockState"(): $BlockState
public "isLava"(): boolean
public "hasColor"(): boolean
public "isIce"(): boolean
public "removeFlags"(arg0: $Collection$Type<($BlockFlag$Type)>): void
public "removeFlags"(...arg0: ($BlockFlag$Type)[]): void
public "setBlockSpritesProxy"(arg0: $IBlockSpritesProxy$Type): void
public "getBlockSpritesProxy"(): $IBlockSpritesProxy
public static "getBlockMDFromChunkLocal"(arg0: $ChunkMD$Type, arg1: integer, arg2: integer, arg3: integer): $BlockMD
public "isFire"(): boolean
get "grass"(): boolean
get "blockStateId"(): string
get "foliage"(): boolean
get "fluid"(): boolean
get "validStateMDs"(): $Set<($BlockMD)>
get "blockId"(): string
get "blockColorProxy"(): $IBlockColorProxy
set "blockColorProxy"(value: $IBlockColorProxy$Type)
get "blockDomain"(): string
get "vanillaBlock"(): boolean
get "name"(): string
get "all"(): $Set<($BlockMD)>
set "color"(value: integer)
get "flags"(): $EnumSet<($BlockFlag)>
get "block"(): $Block
get "ignore"(): boolean
get "allMinecraft"(): $Set<($BlockMD)>
get "textureColor"(): integer
get "water"(): boolean
get "allValid"(): $Set<($BlockMD)>
get "alpha"(): float
set "alpha"(value: float)
get "blockState"(): $BlockState
get "lava"(): boolean
get "ice"(): boolean
set "blockSpritesProxy"(value: $IBlockSpritesProxy$Type)
get "blockSpritesProxy"(): $IBlockSpritesProxy
get "fire"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockMD$Type = ($BlockMD);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockMD_ = $BlockMD$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$TilesKt" {
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $TilesKt {


public static "tilesGet"(arg0: $RouteHandler$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TilesKt$Type = ($TilesKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TilesKt_ = $TilesKt$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/menu/$PopupMenu" {
import {$ScrollPaneScreen, $ScrollPaneScreen$Type} from "packages/journeymap/client/ui/component/$ScrollPaneScreen"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$SelectableParent, $SelectableParent$Type} from "packages/journeymap/client/ui/component/$SelectableParent"
import {$DropDownItem, $DropDownItem$Type} from "packages/journeymap/client/ui/component/$DropDownItem"
import {$Removable, $Removable$Type} from "packages/journeymap/client/ui/component/$Removable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $PopupMenu extends $ScrollPaneScreen implements $Removable, $SelectableParent {
 "visible": boolean
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Fullscreen$Type)
constructor(arg0: $PopupMenu$Type)

public "displayWaypointOptions"(arg0: $BlockPos$Type, arg1: $Waypoint$Type): void
public "closeStack"(): void
public "displayOptions"(arg0: $BlockPos$Type, arg1: $ModPopupMenu$Type): void
public "onClose"(): void
public "onClick"(arg0: $DropDownItem$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "setSelected"(arg0: $DropDownItem$Type): void
public "setClickLoc"(arg0: integer, arg1: integer): void
public "resetPass"(): void
public "isMouseOver"(): boolean
public "onRemove"(): void
public "displayBasicOptions"(arg0: $BlockPos$Type): void
set "selected"(value: $DropDownItem$Type)
get "mouseOver"(): boolean
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
declare module "packages/journeymap/client/ui/component/$SelectableParent" {
import {$DropDownItem, $DropDownItem$Type} from "packages/journeymap/client/ui/component/$DropDownItem"

export interface $SelectableParent {

 "setSelected"(arg0: $DropDownItem$Type): void

(arg0: $DropDownItem$Type): void
}

export namespace $SelectableParent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectableParent$Type = ($SelectableParent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectableParent_ = $SelectableParent$Type;
}}
declare module "packages/journeymap/client/task/main/$DeleteMapTask" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"

export class $DeleteMapTask implements $IMainThreadTask {


public "getName"(): string
public static "queue"(arg0: boolean): void
public "perform"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type): $IMainThreadTask
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeleteMapTask$Type = ($DeleteMapTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeleteMapTask_ = $DeleteMapTask$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$ActionKt" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $ActionKt {


public static "autoMap"(arg0: $RouteHandler$Type, arg1: $Minecraft$Type, arg2: $Level$Type): any
public static "actionGet"(arg0: $RouteHandler$Type): any
public static "saveMap"(arg0: $RouteHandler$Type, arg1: $Minecraft$Type, arg2: $Level$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActionKt$Type = ($ActionKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActionKt_ = $ActionKt$Type;
}}
declare module "packages/journeymap/client/render/ingame/$WaypointBeaconRenderer" {
import {$WaypointRenderer, $WaypointRenderer$Type} from "packages/journeymap/client/render/ingame/$WaypointRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $WaypointBeaconRenderer extends $WaypointRenderer {

constructor()

public "renderBeamSegment"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: float, arg3: long, arg4: integer, arg5: integer, arg6: (float)[], arg7: float, arg8: float, arg9: boolean, arg10: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointBeaconRenderer$Type = ($WaypointBeaconRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointBeaconRenderer_ = $WaypointBeaconRenderer$Type;
}}
declare module "packages/journeymap/client/api/impl/$ClientAPI" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IClientAPI, $IClientAPI$Type} from "packages/journeymap/client/api/$IClientAPI"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$DisplayType, $DisplayType$Type} from "packages/journeymap/client/api/display/$DisplayType"
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/api/display/$Waypoint"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Displayable, $Displayable$Type} from "packages/journeymap/client/api/display/$Displayable"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ClientEventManager, $ClientEventManager$Type} from "packages/journeymap/client/api/impl/$ClientEventManager"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $ClientAPI extends $Enum<($ClientAPI)> implements $IClientAPI {
static readonly "INSTANCE": $ClientAPI


public "remove"(arg0: $Displayable$Type): void
public static "values"(): ($ClientAPI)[]
public static "valueOf"(arg0: string): $ClientAPI
public "removeAll"(arg0: string): void
public "removeAll"(arg0: string, arg1: $DisplayType$Type): void
public "exists"(arg0: $Displayable$Type): boolean
public "purge"(): void
public "getDataPath"(arg0: string): $File
public "setWorldId"(arg0: string): void
public "isDisplayEnabled"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type): boolean
public "isWaypointsEnabled"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type): boolean
public "getWaypoint"(arg0: string, arg1: string): $Waypoint
public "getAllWaypoints"(): $List<($Waypoint)>
public "getAllWaypoints"(arg0: $ResourceKey$Type<($Level$Type)>): $List<($Waypoint)>
public "toggleWaypoints"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type, arg3: boolean): void
public "getWaypoints"(arg0: string): $List<($Waypoint)>
public "playerAccepts"(arg0: string, arg1: $DisplayType$Type): boolean
public "getUIState"(arg0: $Context$UI$Type): $UIState
public "requestMapTile"(arg0: string, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Context$MapType$Type, arg3: $ChunkPos$Type, arg4: $ChunkPos$Type, arg5: integer, arg6: integer, arg7: boolean, arg8: $Consumer$Type<($NativeImage$Type)>): void
public "toggleDisplay"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type, arg3: boolean): void
public "subscribe"(arg0: string, arg1: $EnumSet$Type<($ClientEvent$Type$Type)>): void
public "isDrawStepsUpdateNeeded"(): boolean
public "getWorldId"(): string
public "show"(arg0: $Displayable$Type): void
public "getDrawSteps"(arg0: $List$Type<(any)>, arg1: $UIState$Type): void
public "getClientEventManager"(): $ClientEventManager
public "refreshDataPathCache"(arg0: boolean): void
public "flagOverlaysForRerender"(): void
public "getLastUIState"(): $UIState
set "worldId"(value: string)
get "allWaypoints"(): $List<($Waypoint)>
get "drawStepsUpdateNeeded"(): boolean
get "worldId"(): string
get "clientEventManager"(): $ClientEventManager
get "lastUIState"(): $UIState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientAPI$Type = (("instance")) | ($ClientAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientAPI_ = $ClientAPI$Type;
}}
declare module "packages/journeymap/client/render/map/$RegionRenderer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RegionRenderer {
static "TOGGLED": boolean


public static "render"(arg0: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionRenderer$Type = ($RegionRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionRenderer_ = $RegionRenderer$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgePopupCustomEvents" {
import {$PopupMenuEvent$WaypointPopupMenuEvent, $PopupMenuEvent$WaypointPopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$WaypointPopupMenuEvent"
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$PopupMenuEvent$FullscreenPopupMenuEvent, $PopupMenuEvent$FullscreenPopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$FullscreenPopupMenuEvent"
import {$EntityRadarUpdateEvent, $EntityRadarUpdateEvent$Type} from "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent"

export class $ForgePopupCustomEvents implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onRadarEntityUpdateEvent"(arg0: $EntityRadarUpdateEvent$Type): void
public "onWaypointPopupMenu"(arg0: $PopupMenuEvent$WaypointPopupMenuEvent$Type): void
public "onFullscreenPopupMenu"(arg0: $PopupMenuEvent$FullscreenPopupMenuEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePopupCustomEvents$Type = ($ForgePopupCustomEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePopupCustomEvents_ = $ForgePopupCustomEvents$Type;
}}
declare module "packages/journeymap/client/model/$RegionImageCache" {
import {$RegionImageSet, $RegionImageSet$Type} from "packages/journeymap/client/model/$RegionImageSet"
import {$RegionImageSet$Key, $RegionImageSet$Key$Type} from "packages/journeymap/client/model/$RegionImageSet$Key"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MapState, $MapState$Type} from "packages/journeymap/client/model/$MapState"
import {$CacheBuilder, $CacheBuilder$Type} from "packages/com/google/common/cache/$CacheBuilder"
import {$LoadingCache, $LoadingCache$Type} from "packages/com/google/common/cache/$LoadingCache"

export class $RegionImageCache extends $Enum<($RegionImageCache)> {
static readonly "INSTANCE": $RegionImageCache
 "firstFileFlushIntervalSecs": long
 "flushFileIntervalSecs": long
 "textureCacheAgeSecs": long


public "initRegionImageSetsCache"(arg0: $CacheBuilder$Type<(any), (any)>): $LoadingCache<($RegionImageSet$Key), ($RegionImageSet)>
public static "values"(): ($RegionImageCache)[]
public static "valueOf"(arg0: string): $RegionImageCache
public "clear"(): void
public "flushToDisk"(arg0: boolean): void
public "updateTextures"(arg0: boolean, arg1: boolean): void
public "getLastFlush"(): long
public "getChangedSince"(arg0: $MapType$Type, arg1: long): $List<($RegionCoord)>
public "deleteMap"(arg0: $MapState$Type, arg1: boolean): boolean
public "isDirtySince"(arg0: $RegionCoord$Type, arg1: $MapType$Type, arg2: long): boolean
public "getRegionImageSet"(arg0: $RegionCoord$Type): $RegionImageSet
public "getRegionImageSet"(arg0: $RegionImageSet$Key$Type): $RegionImageSet
public "getRegionImageSet"(arg0: $ChunkMD$Type, arg1: $MapType$Type): $RegionImageSet
public "flushToDiskAsync"(arg0: boolean): void
get "lastFlush"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionImageCache$Type = (("instance")) | ($RegionImageCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionImageCache_ = $RegionImageCache$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Container$Toolbar$ToolbarSpec" {
import {$Theme$ImageSpec, $Theme$ImageSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ImageSpec"

export class $Theme$Container$Toolbar$ToolbarSpec {
 "useThemeImages": boolean
 "prefix": string
 "margin": integer
 "padding": integer
 "begin": $Theme$ImageSpec
 "inner": $Theme$ImageSpec
 "end": $Theme$ImageSpec

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Container$Toolbar$ToolbarSpec$Type = ($Theme$Container$Toolbar$ToolbarSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Container$Toolbar$ToolbarSpec_ = $Theme$Container$Toolbar$ToolbarSpec$Type;
}}
declare module "packages/journeymap/client/api/display/$MarkerOverlay" {
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MapImage, $MapImage$Type} from "packages/journeymap/client/api/model/$MapImage"

export class $MarkerOverlay extends $Overlay {

constructor(arg0: string, arg1: string, arg2: $BlockPos$Type, arg3: $MapImage$Type)

public "toString"(): string
public "getPoint"(): $BlockPos
public "getIcon"(): $MapImage
public "setPoint"(arg0: $BlockPos$Type): $MarkerOverlay
public "setIcon"(arg0: $MapImage$Type): $MarkerOverlay
get "point"(): $BlockPos
get "icon"(): $MapImage
set "point"(value: $BlockPos$Type)
set "icon"(value: $MapImage$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MarkerOverlay$Type = ($MarkerOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MarkerOverlay_ = $MarkerOverlay$Type;
}}
declare module "packages/journeymap/client/model/$GridSpecs" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$GridSpec, $GridSpec$Type} from "packages/journeymap/client/model/$GridSpec"

export class $GridSpecs {
static readonly "DEFAULT_DAY": $GridSpec
static readonly "DEFAULT_NIGHT": $GridSpec
static readonly "DEFAULT_UNDERGROUND": $GridSpec

constructor()
constructor(arg0: $GridSpec$Type, arg1: $GridSpec$Type, arg2: $GridSpec$Type)

public "clone"(): $GridSpecs
public "setSpec"(arg0: $MapType$Type, arg1: $GridSpec$Type): void
public "updateFrom"(arg0: $GridSpecs$Type): void
public "getSpec"(arg0: $MapType$Type): $GridSpec
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridSpecs$Type = ($GridSpecs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridSpecs_ = $GridSpecs$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$CustomToolbarEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$CustomToolBarBuilder, $CustomToolBarBuilder$Type} from "packages/journeymap/client/api/display/$CustomToolBarBuilder"

/**
 * 
 * @deprecated
 */
export class $FullscreenDisplayEvent$CustomToolbarEvent extends $Event {

constructor()
constructor(arg0: $IFullscreen$Type, arg1: $CustomToolBarBuilder$Type)

public "isCancelable"(): boolean
public "getFullscreen"(): $IFullscreen
public "getCustomToolBarBuilder"(): $CustomToolBarBuilder
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "fullscreen"(): $IFullscreen
get "customToolBarBuilder"(): $CustomToolBarBuilder
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenDisplayEvent$CustomToolbarEvent$Type = ($FullscreenDisplayEvent$CustomToolbarEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenDisplayEvent$CustomToolbarEvent_ = $FullscreenDisplayEvent$CustomToolbarEvent$Type;
}}
declare module "packages/journeymap/client/event/handlers/$ChunkMonitorHandler" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ChunkMonitorHandler {


public static "getInstance"(): $ChunkMonitorHandler
public "reset"(): void
public "onChunkUpdate"(arg0: $LevelAccessor$Type, arg1: $ChunkPos$Type): void
public "onBlockUpdate"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): void
public "resetRenderTimes"(arg0: long): void
public "onWorldUnload"(arg0: $LevelAccessor$Type): void
public "onChunkLoad"(arg0: $LevelAccessor$Type, arg1: $ChunkAccess$Type): void
get "instance"(): $ChunkMonitorHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMonitorHandler$Type = ($ChunkMonitorHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMonitorHandler_ = $ChunkMonitorHandler$Type;
}}
declare module "packages/journeymap/client/model/$GridSpec" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$GridSpec$Style, $GridSpec$Style$Type} from "packages/journeymap/client/model/$GridSpec$Style"

export class $GridSpec {
readonly "style": $GridSpec$Style
readonly "red": float
readonly "green": float
readonly "blue": float
readonly "alpha": float

constructor(arg0: $GridSpec$Style$Type, arg1: $Color$Type, arg2: float)
constructor(arg0: $GridSpec$Style$Type, arg1: float, arg2: float, arg3: float, arg4: float)

public "clone"(): $GridSpec
public "setColorCoords"(arg0: integer, arg1: integer): $GridSpec
public "getColorY"(): integer
public "getColorX"(): integer
public "beginTexture"(arg0: integer, arg1: integer, arg2: float): void
public "finishTexture"(): void
public "getTexture"(): $Texture
public "getColor"(): integer
get "colorY"(): integer
get "colorX"(): integer
get "texture"(): $Texture
get "color"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridSpec$Type = ($GridSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridSpec_ = $GridSpec$Type;
}}
declare module "packages/journeymap/client/ui/component/$SearchTextBox" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$TextBoxButton, $TextBoxButton$Type} from "packages/journeymap/client/ui/component/$TextBoxButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $SearchTextBox extends $TextBoxButton {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: string, arg1: $Font$Type, arg2: integer, arg3: integer)

public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchTextBox$Type = ($SearchTextBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchTextBox_ = $SearchTextBox$Type;
}}
declare module "packages/journeymap/client/api/display/$IWaypointDisplay" {
import {$MapImage, $MapImage$Type} from "packages/journeymap/client/api/model/$MapImage"

export interface $IWaypointDisplay {

 "getBackgroundColor"(): integer
 "getDisplayDimensions"(): (string)[]
 "getIcon"(): $MapImage
 "getColor"(): integer
}

export namespace $IWaypointDisplay {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWaypointDisplay$Type = ($IWaypointDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWaypointDisplay_ = $IWaypointDisplay$Type;
}}
declare module "packages/journeymap/client/task/multi/$ApiImageTask" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ApiImageTask implements $Runnable {

constructor(arg0: string, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Context$MapType$Type, arg3: $ChunkPos$Type, arg4: $ChunkPos$Type, arg5: integer, arg6: integer, arg7: boolean, arg8: $Consumer$Type<($NativeImage$Type)>)

public "run"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ApiImageTask$Type = ($ApiImageTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ApiImageTask_ = $ApiImageTask$Type;
}}
declare module "packages/journeymap/client/api/display/$ImageOverlay" {
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MapImage, $MapImage$Type} from "packages/journeymap/client/api/model/$MapImage"

export class $ImageOverlay extends $Overlay {

constructor(arg0: string, arg1: string, arg2: $BlockPos$Type, arg3: $BlockPos$Type, arg4: $MapImage$Type)

public "toString"(): string
public "setImage"(arg0: $MapImage$Type): $ImageOverlay
public "setSouthEastPoint"(arg0: $BlockPos$Type): $ImageOverlay
public "setNorthWestPoint"(arg0: $BlockPos$Type): $ImageOverlay
public "getNorthWestPoint"(): $BlockPos
public "getSouthEastPoint"(): $BlockPos
public "getImage"(): $MapImage
set "image"(value: $MapImage$Type)
set "southEastPoint"(value: $BlockPos$Type)
set "northWestPoint"(value: $BlockPos$Type)
get "northWestPoint"(): $BlockPos
get "southEastPoint"(): $BlockPos
get "image"(): $MapImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageOverlay$Type = ($ImageOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageOverlay_ = $ImageOverlay$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$WaypointEditor" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointEditor extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Waypoint$Type, arg1: boolean, arg2: $JmUI$Type, arg3: boolean)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointEditor$Type = ($WaypointEditor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointEditor_ = $WaypointEditor$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$StatusKt" {
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $StatusKt {


public static "statusGet"(arg0: $RouteHandler$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatusKt$Type = ($StatusKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatusKt_ = $StatusKt$Type;
}}
declare module "packages/journeymap/client/api/impl/$ModPopupMenuImpl$SubMenuAction" {
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$ModPopupMenu$Action, $ModPopupMenu$Action$Type} from "packages/journeymap/client/api/display/$ModPopupMenu$Action"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $ModPopupMenuImpl$SubMenuAction extends $ModPopupMenu$Action {

 "doAction"(arg0: $BlockPos$Type): void
 "doAction"(arg0: $BlockPos$Type, arg1: $Button$Type): void
 "onHoverState"(arg0: $BlockPos$Type, arg1: $Button$Type, arg2: boolean): void
}

export namespace $ModPopupMenuImpl$SubMenuAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModPopupMenuImpl$SubMenuAction$Type = ($ModPopupMenuImpl$SubMenuAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModPopupMenuImpl$SubMenuAction_ = $ModPopupMenuImpl$SubMenuAction$Type;
}}
declare module "packages/journeymap/client/feature/$Feature" {
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $Feature extends $Enum<($Feature)> {
static readonly "RadarPlayers": $Feature
static readonly "RadarAnimals": $Feature
static readonly "RadarMobs": $Feature
static readonly "RadarVillagers": $Feature
static readonly "MapTopo": $Feature
static readonly "MapSurface": $Feature
static readonly "MapCaves": $Feature
static readonly "MapBiome": $Feature


public static "values"(): ($Feature)[]
public static "valueOf"(arg0: string): $Feature
public static "all"(): $EnumSet<($Feature)>
public static "fromApiMapType"(arg0: $Context$MapType$Type, arg1: $ResourceKey$Type<($Level$Type)>): $Feature
public static "radar"(): $EnumSet<($Feature)>
public static "fromMapType"(arg0: $MapType$Type): $Feature
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Feature$Type = (("radarplayers") | ("mapsurface") | ("radaranimals") | ("radarmobs") | ("radarvillagers") | ("mapbiome") | ("mapcaves") | ("maptopo")) | ($Feature);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Feature_ = $Feature$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/layer/$LayerDelegate" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"

export class $LayerDelegate {

constructor(arg0: $Fullscreen$Type)

public "onMouseClicked"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: integer, arg4: float): void
public "onMouseMove"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: float, arg4: boolean): void
public "getDrawSteps"(): $List<($DrawStep)>
public "getBlockPos"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type): $BlockPos
get "drawSteps"(): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayerDelegate$Type = ($LayerDelegate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayerDelegate_ = $LayerDelegate$Type;
}}
declare module "packages/journeymap/client/ui/component/$Button" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$ButtonList, $ButtonList$Type} from "packages/journeymap/client/ui/component/$ButtonList"
import {$Button as $Button$0, $Button$Type as $Button$0$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$DrawUtil$VAlign, $DrawUtil$VAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$VAlign"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Button$HoverState, $Button$HoverState$Type} from "packages/journeymap/client/ui/component/$Button$HoverState"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SoundManager, $SoundManager$Type} from "packages/net/minecraft/client/sounds/$SoundManager"
import {$DrawUtil$HAlign, $DrawUtil$HAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$HAlign"

export class $Button extends $Button$0 {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: integer, arg1: integer, arg2: string, arg3: $Button$OnPress$Type)
constructor(arg0: string, arg1: $Button$OnPress$Type)
constructor(arg0: string)

public "toString"(): string
public "getBounds"(): $Rectangle2D$Double
public "getDisplayString"(): string
public "leftOf"(arg0: integer): $Button
public "leftOf"(arg0: $Button$Type, arg1: integer): $Button
public "rightOf"(arg0: integer): $Button
public "rightOf"(arg0: $Button$Type, arg1: integer): $Button
public "isEnabled"(): boolean
public "refresh"(): void
public "setTextOnly"(arg0: $Font$Type): void
public "fitWidth"(arg0: $Font$Type): void
public "getBottomY"(): integer
public "resetLabelColors"(): void
public "setEnabled"(arg0: boolean): void
public "setVisible"(arg0: boolean): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "isActive"(): boolean
public "isVisible"(): boolean
public "setHovered"(arg0: boolean): void
public "setTooltip"(arg0: integer, ...arg1: (string)[]): void
public "setTooltip"(...arg0: (string)[]): void
public "getFormattedTooltip"(): $List<($FormattedCharSequence)>
public "drawCenteredString"(arg0: $GuiGraphics$Type, arg1: $Font$Type, arg2: string, arg3: float, arg4: float, arg5: integer): void
public "above"(arg0: $Button$Type, arg1: integer): $Button
public "above"(arg0: integer): $Button
public "below"(arg0: integer): $Button
public "below"(arg0: $Button$Type, arg1: integer): $Button
public "below"(arg0: $ButtonList$Type, arg1: integer): $Button
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mousePressed"(arg0: double, arg1: double, arg2: boolean): boolean
public "setDrawButton"(arg0: boolean): void
public "setLabelColors"(arg0: integer, arg1: integer, arg2: integer): void
public "setDrawBackground"(arg0: boolean): void
public "setDefaultStyle"(arg0: boolean): void
public "getFitWidth"(arg0: $Font$Type): integer
public "addClickListener"(arg0: $Function$Type<($Button$Type), (boolean)>): void
public "alignTo"(arg0: $Button$Type, arg1: $DrawUtil$HAlign$Type, arg2: integer, arg3: $DrawUtil$VAlign$Type, arg4: integer): $Button
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "isDrawBackground"(): boolean
public "setMouseOver"(arg0: boolean): void
public "getActiveColor"(): integer
public "drawHovered"(arg0: boolean): void
public "setDrawFrame"(arg0: boolean): void
public static "emptyPressable"(): $Button$OnPress
public "isDrawFrame"(): boolean
public "getMiddleY"(): integer
public "getScrollableWidth"(): integer
public "setDrawLabelShadow"(arg0: boolean): void
public "getButtonHeight"(): integer
public "centerVerticalOn"(arg0: integer): $Button
public "setOnHover"(arg0: $Button$HoverState$Type): void
public "isDefaultStyle"(): boolean
public "setScrollableWidth"(arg0: integer): void
public "drawScrollable"(arg0: $GuiGraphics$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer): void
public "clickScrollable"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): void
public "secondaryDrawButton"(): void
public "setDrawBackgroundOnDisable"(arg0: boolean): void
public "setScrollablePosition"(arg0: integer, arg1: integer): void
public "centerHorizontalOn"(arg0: integer): $Button
public "getCenterX"(): integer
public "getRightX"(): integer
public "playDownSound"(arg0: $SoundManager$Type): void
public "setHeight"(arg0: integer): void
public "isHovered"(): boolean
public "setWidth"(arg0: integer): void
public "setX"(arg0: integer): void
public "setY"(arg0: integer): void
public "onPress"(): void
public "getLabel"(): string
public "isMouseOver"(): boolean
public "setBackgroundColors"(arg0: integer, arg1: integer, arg2: integer): void
public "checkClickListeners"(): boolean
public "mouseOver"(arg0: double, arg1: double): boolean
public "getLabelColor"(): integer
public "drawUnderline"(arg0: $PoseStack$Type): void
public "showDisabledOnHover"(arg0: boolean): void
public "setHorizontalAlignment"(arg0: $DrawUtil$HAlign$Type): void
public "isDrawBackgroundOnDisable"(): boolean
public "drawPartialScrollable"(arg0: $GuiGraphics$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "getUnformattedTooltip"(): string
public "renderSpecialDecoration"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
get "bounds"(): $Rectangle2D$Double
get "displayString"(): string
get "enabled"(): boolean
set "textOnly"(value: $Font$Type)
get "bottomY"(): integer
set "enabled"(value: boolean)
set "visible"(value: boolean)
get "active"(): boolean
get "visible"(): boolean
set "hovered"(value: boolean)
set "tooltip"(value: (string)[])
get "formattedTooltip"(): $List<($FormattedCharSequence)>
set "drawButton"(value: boolean)
set "drawBackground"(value: boolean)
set "defaultStyle"(value: boolean)
get "drawBackground"(): boolean
get "activeColor"(): integer
set "drawFrame"(value: boolean)
get "drawFrame"(): boolean
get "middleY"(): integer
get "scrollableWidth"(): integer
set "drawLabelShadow"(value: boolean)
get "buttonHeight"(): integer
set "onHover"(value: $Button$HoverState$Type)
get "defaultStyle"(): boolean
set "scrollableWidth"(value: integer)
set "drawBackgroundOnDisable"(value: boolean)
get "centerX"(): integer
get "rightX"(): integer
set "height"(value: integer)
get "hovered"(): boolean
set "width"(value: integer)
set "x"(value: integer)
set "y"(value: integer)
get "label"(): string
get "labelColor"(): integer
set "horizontalAlignment"(value: $DrawUtil$HAlign$Type)
get "drawBackgroundOnDisable"(): boolean
get "unformattedTooltip"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Button$Type = ($Button);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Button_ = $Button$Type;
}}
declare module "packages/journeymap/client/render/$RenderWrapper" {
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RenderWrapper {
static readonly "GL_TEXTURE_2D": integer
static readonly "GL_TEXTURE_MIN_FILTER": integer
static readonly "GL_LINEAR": integer
static readonly "GL_TEXTURE_MAG_FILTER": integer
static readonly "GL_TEXTURE_WRAP_S": integer
static readonly "GL_REPEAT": integer
static readonly "GL_TEXTURE_WRAP_T": integer
static readonly "GL_SRC_ALPHA": integer
static readonly "GL_ONE_MINUS_SRC_ALPHA": integer
static readonly "GL_ZERO": integer
static readonly "GL_DEPTH_BUFFER_BIT": integer
static readonly "GL_GREATER": integer
static readonly "GL_NEAREST": integer
static readonly "GL_NO_ERROR": integer
static readonly "GL_LEQUAL": integer
static readonly "GL_GEQUAL": integer
static readonly "GL_VIEWPORT": integer
static readonly "GL_BGRA": integer
static readonly "GL_RGBA": integer
static readonly "GL_CLAMP_TO_EDGE": integer
static readonly "GL_MIRRORED_REPEAT": integer
static readonly "errorCheck": boolean

constructor()

public static "texImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $IntBuffer$Type): void
public static "checkGLError"(arg0: string): boolean
public static "getIntegerv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "blitFramebuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer): void
public static "texSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "clear"(arg0: integer): void
public static "setShaderTexture"(arg0: integer, arg1: $ResourceLocation$Type): void
public static "setShaderTexture"(arg0: integer, arg1: integer): void
public static "setShader"(arg0: $Supplier$Type<($ShaderInstance$Type)>): void
public static "getError"(): integer
public static "setColor4f"(arg0: float, arg1: float, arg2: float, arg3: float): void
public static "texParameter"(arg0: integer, arg1: integer, arg2: integer): void
public static "clearColor"(arg0: float, arg1: float, arg2: float, arg3: float): void
public static "pixelStore"(arg0: integer, arg1: integer): void
public static "blendFuncSeparate"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "lineWidth"(arg0: float): void
public static "enableCull"(): void
public static "disableCull"(): void
public static "enableBlend"(): void
public static "disableDepthTest"(): void
public static "depthMask"(arg0: boolean): void
public static "blendFunc"(arg0: integer, arg1: integer): void
public static "defaultBlendFunc"(): void
public static "disableBlend"(): void
public static "enableDepthTest"(): void
public static "getModelViewStack"(): $PoseStack
public static "bindFramebuffer"(arg0: integer, arg1: integer): void
public static "enableTexture"(): void
public static "disableTexture"(): void
public static "setProjectionMatrix"(arg0: $Matrix4f$Type): void
public static "activeTexture"(arg0: integer): void
public static "bindTexture"(arg0: integer): void
public static "depthFunc"(arg0: integer): void
public static "colorMask"(arg0: boolean, arg1: boolean, arg2: boolean, arg3: boolean): void
public static "getProjectionMatrix"(): $Matrix4f
public static "bindTextureForSetup"(arg0: integer): void
set "shader"(value: $Supplier$Type<($ShaderInstance$Type)>)
get "error"(): integer
get "modelViewStack"(): $PoseStack
set "projectionMatrix"(value: $Matrix4f$Type)
get "projectionMatrix"(): $Matrix4f
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderWrapper$Type = ($RenderWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderWrapper_ = $RenderWrapper$Type;
}}
declare module "packages/journeymap/client/cartography/$ChunkRenderController" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$BaseRenderer, $BaseRenderer$Type} from "packages/journeymap/client/cartography/render/$BaseRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"

export class $ChunkRenderController {

constructor()

public "renderChunk"(arg0: $RegionCoord$Type, arg1: $MapType$Type, arg2: $ChunkMD$Type, arg3: $RegionData$Type): boolean
public "getRenderer"(arg0: $RegionCoord$Type, arg1: $MapType$Type, arg2: $ChunkMD$Type): $BaseRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRenderController$Type = ($ChunkRenderController);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRenderController_ = $ChunkRenderController$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$SkinKt" {
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $SkinKt {


public static "skinGet"(arg0: $RouteHandler$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkinKt$Type = ($SkinKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkinKt_ = $SkinKt$Type;
}}
declare module "packages/journeymap/client/model/$ChunkMD" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$BlockDataArrays, $BlockDataArrays$Type} from "packages/journeymap/client/model/$BlockDataArrays"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockDataArrays$DataArray, $BlockDataArrays$DataArray$Type} from "packages/journeymap/client/model/$BlockDataArrays$DataArray"

export class $ChunkMD {
static readonly "PROP_IS_SLIME_CHUNK": string
static readonly "PROP_LOADED": string
static readonly "PROP_LAST_RENDERED": string

constructor(arg0: $LevelChunk$Type, arg1: boolean)
constructor(arg0: $LevelChunk$Type, arg1: boolean, arg2: ((((byte)[])[])[])[])
constructor(arg0: $LevelChunk$Type)

public "getProperty"(arg0: string): $Serializable
public "getProperty"(arg0: string, arg1: $Serializable$Type): $Serializable
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "setProperty"(arg0: string, arg1: $Serializable$Type): $Serializable
public "ceiling"(arg0: integer, arg1: integer): integer
public "toWorldX"(arg0: integer): integer
public "toWorldZ"(arg0: integer): integer
public "getChunkBlockState"(arg0: $BlockPos$Type): $BlockState
public "getBlockMD"(arg0: integer, arg1: integer, arg2: integer): $BlockMD
public "getBlockMD"(arg0: $BlockPos$Type): $BlockMD
public "getSavedLightValue"(arg0: integer, arg1: integer, arg2: integer): integer
public "setRendered"(arg0: $MapType$Type): long
public "getBlockDataFloats"(arg0: $MapType$Type): $BlockDataArrays$DataArray<(float)>
public "getLastRendered"(arg0: $MapType$Type): long
public "resetBlockData"(arg0: $MapType$Type): void
public "getLongCoord"(): long
public "resetRenderTimes"(): void
public "getBlockData"(): $BlockDataArrays
public "hasChunk"(): boolean
public "getLoaded"(): long
public "resetRenderTime"(arg0: $MapType$Type): void
public "canBlockSeeTheSky"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getBlockDataInts"(arg0: $MapType$Type): $BlockDataArrays$DataArray<(integer)>
public "hasNoSky"(): boolean
public "getLightOpacity"(arg0: $BlockMD$Type, arg1: integer, arg2: integer, arg3: integer): integer
public "hasRetainedChunk"(): boolean
public "fromNbt"(): boolean
public "stopChunkRetention"(): void
public "getDimension"(): $ResourceKey<($Level)>
public "getMinY"(): integer
public "getBiome"(arg0: $BlockPos$Type): $Biome
public "getWorld"(): $ClientLevel
public "getHeight"(arg0: $BlockPos$Type): integer
public "getCoord"(): $ChunkPos
public "getWorldActualHeight"(): integer
public "getPrecipitationHeight"(arg0: $BlockPos$Type): integer
public "getPrecipitationHeight"(arg0: integer, arg1: integer): integer
public "getBlockDataBooleans"(arg0: $MapType$Type): $BlockDataArrays$DataArray<(boolean)>
public "getBlockPos"(arg0: integer, arg1: integer, arg2: integer): $BlockPos
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "getBlockState"(arg0: integer, arg1: integer, arg2: integer): $BlockState
public "getChunk"(): $LevelChunk
public static "isSlimeChunk"(arg0: $LevelChunk$Type): boolean
set "rendered"(value: $MapType$Type)
get "longCoord"(): long
get "blockData"(): $BlockDataArrays
get "loaded"(): long
get "dimension"(): $ResourceKey<($Level)>
get "minY"(): integer
get "world"(): $ClientLevel
get "coord"(): $ChunkPos
get "worldActualHeight"(): integer
get "chunk"(): $LevelChunk
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMD$Type = ($ChunkMD);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMD_ = $ChunkMD$Type;
}}
declare module "packages/journeymap/client/waypoint/$WaypointParser" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointParser {
static "QUOTES": (string)[]
static "PATTERN": $Pattern

constructor()

public static "getWaypointStrings"(arg0: string): $List<(string)>
public static "parse"(arg0: string): $Waypoint
public static "getWaypoints"(arg0: string): $List<($Waypoint)>
public static "parseChatForWaypoints"(arg0: $Component$Type, arg1: string): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointParser$Type = ($WaypointParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointParser_ = $WaypointParser$Type;
}}
declare module "packages/journeymap/client/properties/$TopoProperties" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$StringField, $StringField$Type} from "packages/journeymap/common/properties/config/$StringField"
import {$ClientPropertiesBase, $ClientPropertiesBase$Type} from "packages/journeymap/client/properties/$ClientPropertiesBase"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"

export class $TopoProperties extends $ClientPropertiesBase implements $Comparable<($TopoProperties)> {
readonly "showContour": $BooleanField
readonly "landContour": $StringField
readonly "waterContour": $StringField
readonly "land": $StringField
readonly "water": $StringField

constructor()

public "getName"(): string
public "compareTo"(arg0: $TopoProperties$Type): integer
public "isValid"(arg0: boolean): boolean
public "getHeaders"(): (string)[]
public "getLandColors"(): (integer)[]
public "getWaterColors"(): (integer)[]
public "updateFrom"<T extends $PropertiesBase>(arg0: T): void
public "getWaterContourColor"(): integer
public "getLandContourColor"(): integer
get "name"(): string
get "headers"(): (string)[]
get "landColors"(): (integer)[]
get "waterColors"(): (integer)[]
get "waterContourColor"(): integer
get "landContourColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TopoProperties$Type = ($TopoProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TopoProperties_ = $TopoProperties$Type;
}}
declare module "packages/journeymap/client/mod/impl/$Bibliocraft" {
import {$IBlockSpritesProxy, $IBlockSpritesProxy$Type} from "packages/journeymap/client/mod/$IBlockSpritesProxy"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$ColoredSprite, $ColoredSprite$Type} from "packages/journeymap/client/cartography/color/$ColoredSprite"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $Bibliocraft implements $IModBlockHandler, $IBlockSpritesProxy {

constructor()

public "initialize"(arg0: $BlockMD$Type): void
public "getSprites"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): $Collection<($ColoredSprite)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bibliocraft$Type = ($Bibliocraft);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bibliocraft_ = $Bibliocraft$Type;
}}
declare module "packages/journeymap/client/waypoint/$WaypointEventManager" {
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointEventManager {

constructor()

public static "createWaypointEvent"(arg0: $Waypoint$Type): void
public static "deleteWaypointEvent"(arg0: $Waypoint$Type): void
public static "updateWaypointEvent"(arg0: $Waypoint$Type): void
public static "readWaypointEvent"(arg0: $Waypoint$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointEventManager$Type = ($WaypointEventManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointEventManager_ = $WaypointEventManager$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$EntityDisplay" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $EntityDisplay extends $Enum<($EntityDisplay)> implements $KeyedEnum {
static readonly "LargeDots": $EntityDisplay
static readonly "LargeIcons": $EntityDisplay
readonly "key": string


public "toString"(): string
public static "values"(): ($EntityDisplay)[]
public static "valueOf"(arg0: string): $EntityDisplay
public "getKey"(): string
public static "getLocatorTexture"(arg0: $EntityDisplay$Type, arg1: boolean): $Texture
public "getDot"(): $EntityDisplay
public "isDots"(): boolean
public "isLarge"(): boolean
public static "getEntityTexture"(arg0: $EntityDisplay$Type): $Texture
public static "getEntityTexture"(arg0: $EntityDisplay$Type, arg1: $UUID$Type, arg2: string): $Texture
public static "getEntityTexture"(arg0: $EntityDisplay$Type, arg1: $ResourceLocation$Type): $Texture
get "key"(): string
get "dot"(): $EntityDisplay
get "dots"(): boolean
get "large"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityDisplay$Type = (("largedots") | ("largeicons")) | ($EntityDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityDisplay_ = $EntityDisplay$Type;
}}
declare module "packages/journeymap/client/render/$RenderFacade" {
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $RenderFacade extends $EntityRenderer<(any)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "render"(arg0: $Entity$Type, arg1: float, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void
public "getTextureLocation"(arg0: $Entity$Type): $ResourceLocation
public static "getEntityTexture"(arg0: $EntityRenderer$Type<(any)>, arg1: $Entity$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderFacade$Type = ($RenderFacade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderFacade_ = $RenderFacade$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$MapTypeButtonDisplayEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ThemeButtonDisplay, $ThemeButtonDisplay$Type} from "packages/journeymap/client/api/display/$ThemeButtonDisplay"
import {$FullscreenDisplayEvent, $FullscreenDisplayEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent"

/**
 * 
 * @deprecated
 */
export class $FullscreenDisplayEvent$MapTypeButtonDisplayEvent extends $FullscreenDisplayEvent {

constructor(arg0: $IFullscreen$Type, arg1: $ThemeButtonDisplay$Type)
constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenDisplayEvent$MapTypeButtonDisplayEvent$Type = ($FullscreenDisplayEvent$MapTypeButtonDisplayEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenDisplayEvent$MapTypeButtonDisplayEvent_ = $FullscreenDisplayEvent$MapTypeButtonDisplayEvent$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeClientTickEvent" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $ForgeClientTickEvent implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onClientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientTickEvent$Type = ($ForgeClientTickEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientTickEvent_ = $ForgeClientTickEvent$Type;
}}
declare module "packages/journeymap/client/texture/$DynamicTextureImpl" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DynamicTexture, $DynamicTexture$Type} from "packages/net/minecraft/client/renderer/texture/$DynamicTexture"

export class $DynamicTextureImpl extends $DynamicTexture implements $Texture {
static readonly "NOT_ASSIGNED": integer

constructor(arg0: integer, arg1: integer, arg2: boolean)
constructor(arg0: $NativeImage$Type, arg1: $ResourceLocation$Type)
constructor(arg0: $NativeImage$Type)

public "remove"(): void
public "getLocation"(): $ResourceLocation
public "release"(): void
public "setDescription"(arg0: string): void
public "getWidth"(): integer
public "getHeight"(): integer
public "upload"(): void
public "setDisplayHeight"(arg0: integer): void
public "getNativeImage"(): $NativeImage
public "getScaledImage"(arg0: float): $Texture
public "setNativeImage"(arg0: $NativeImage$Type): void
public "setDisplayWidth"(arg0: integer): void
public "getTextureId"(): integer
public "getRGB"(arg0: integer, arg1: integer): integer
public "getAlpha"(): float
public "setAlpha"(arg0: float): void
public "hasImage"(): boolean
public "setNativeImage"(arg0: $NativeImage$Type, arg1: boolean): void
get "location"(): $ResourceLocation
set "description"(value: string)
get "width"(): integer
get "height"(): integer
set "displayHeight"(value: integer)
get "nativeImage"(): $NativeImage
set "nativeImage"(value: $NativeImage$Type)
set "displayWidth"(value: integer)
get "textureId"(): integer
get "alpha"(): float
set "alpha"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicTextureImpl$Type = ($DynamicTextureImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicTextureImpl_ = $DynamicTextureImpl$Type;
}}
declare module "packages/journeymap/client/mod/impl/$ChinjufuMod" {
import {$IBlockSpritesProxy, $IBlockSpritesProxy$Type} from "packages/journeymap/client/mod/$IBlockSpritesProxy"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$ColoredSprite, $ColoredSprite$Type} from "packages/journeymap/client/cartography/color/$ColoredSprite"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ChinjufuMod implements $IModBlockHandler, $IBlockSpritesProxy {

constructor()

public "initialize"(arg0: $BlockMD$Type): void
public "getSprites"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): $Collection<($ColoredSprite)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChinjufuMod$Type = ($ChinjufuMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChinjufuMod_ = $ChinjufuMod$Type;
}}
declare module "packages/journeymap/client/task/main/$EnsureCurrentColorsTask" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"

export class $EnsureCurrentColorsTask implements $IMainThreadTask {

constructor()
constructor(arg0: boolean, arg1: boolean)

public "getName"(): string
public "perform"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type): $IMainThreadTask
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnsureCurrentColorsTask$Type = ($EnsureCurrentColorsTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnsureCurrentColorsTask_ = $EnsureCurrentColorsTask$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$EntityRadarUpdateEvent$EntityType, $EntityRadarUpdateEvent$EntityType$Type} from "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent$EntityType"
import {$WrappedEntity, $WrappedEntity$Type} from "packages/journeymap/client/api/model/$WrappedEntity"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $EntityRadarUpdateEvent extends $Event {

constructor(arg0: $UIState$Type, arg1: $EntityRadarUpdateEvent$EntityType$Type, arg2: $WrappedEntity$Type)
constructor()

public "getType"(): $EntityRadarUpdateEvent$EntityType
public "isCancelable"(): boolean
public "getActiveUiState"(): $UIState
public "getWrappedEntity"(): $WrappedEntity
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "type"(): $EntityRadarUpdateEvent$EntityType
get "cancelable"(): boolean
get "activeUiState"(): $UIState
get "wrappedEntity"(): $WrappedEntity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRadarUpdateEvent$Type = ($EntityRadarUpdateEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRadarUpdateEvent_ = $EntityRadarUpdateEvent$Type;
}}
declare module "packages/journeymap/client/io/$PngjHelper" {
import {$File, $File$Type} from "packages/java/io/$File"

export class $PngjHelper {

constructor()

public static "mergeFiles"(arg0: ($File$Type)[], arg1: $File$Type, arg2: integer, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngjHelper$Type = ($PngjHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngjHelper_ = $PngjHelper$Type;
}}
declare module "packages/journeymap/client/api/event/$WaypointEvent$Context" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WaypointEvent$Context extends $Enum<($WaypointEvent$Context)> {
static readonly "CREATE": $WaypointEvent$Context
static readonly "UPDATE": $WaypointEvent$Context
static readonly "DELETED": $WaypointEvent$Context
static readonly "READ": $WaypointEvent$Context


public static "values"(): ($WaypointEvent$Context)[]
public static "valueOf"(arg0: string): $WaypointEvent$Context
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointEvent$Context$Type = (("deleted") | ("read") | ("create") | ("update")) | ($WaypointEvent$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointEvent$Context_ = $WaypointEvent$Context$Type;
}}
declare module "packages/journeymap/client/cartography/color/$ColorPalette" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"

export class $ColorPalette {
static readonly "HELP_PAGE": string
static readonly "SAMPLE_STANDARD_PATH": string
static readonly "SAMPLE_WORLD_PATH": string
static readonly "JSON_FILENAME": string
static readonly "HTML_FILENAME": string
static readonly "VARIABLE": string
static readonly "UTF8": $Charset
static readonly "VERSION": double


public "toString"(): string
public "size"(): integer
public static "create"(arg0: boolean, arg1: boolean): $ColorPalette
public "isDirty"(): boolean
public "getVersion"(): double
public "writeToFile"(): void
public "isPermanent"(): boolean
public "applyColors"(arg0: $Collection$Type<($BlockMD$Type)>, arg1: boolean): integer
public "applyColor"(arg0: $BlockMD$Type, arg1: boolean): boolean
public "isStandard"(): boolean
public "setPermanent"(arg0: boolean): void
public "getOriginHtml"(arg0: boolean, arg1: boolean): $File
public static "getActiveColorPalette"(): $ColorPalette
public "getOrigin"(): $File
get "dirty"(): boolean
get "version"(): double
get "permanent"(): boolean
get "standard"(): boolean
set "permanent"(value: boolean)
get "activeColorPalette"(): $ColorPalette
get "origin"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorPalette$Type = ($ColorPalette);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorPalette_ = $ColorPalette$Type;
}}
declare module "packages/journeymap/client/ui/option/$ButtonListSlot" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CategorySlot, $CategorySlot$Type} from "packages/journeymap/client/ui/option/$CategorySlot"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Slot, $Slot$Type} from "packages/journeymap/client/ui/component/$Slot"

export class $ButtonListSlot extends $Slot implements $Comparable<($ButtonListSlot)> {

constructor(arg0: $CategorySlot$Type)

public "add"(arg0: $SlotMetadata$Type<(any)>): $ButtonListSlot
public "compareTo"(arg0: $ButtonListSlot$Type): integer
public "clear"(): void
public "contains"(arg0: $SlotMetadata$Type<(any)>): boolean
public "addAll"(arg0: $Collection$Type<($SlotMetadata$Type)>): $ButtonListSlot
public "merge"(arg0: $ButtonListSlot$Type): $ButtonListSlot
public "getLastPressed"(): $SlotMetadata<(any)>
public "setEnabled"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "getColumnWidth"(): integer
public "getMetadata"(): $Collection<($SlotMetadata)>
public "getCurrentTooltip"(): $SlotMetadata<(any)>
public "getChildSlots"(arg0: integer, arg1: integer): $List<($Slot)>
get "lastPressed"(): $SlotMetadata<(any)>
set "enabled"(value: boolean)
get "columnWidth"(): integer
get "metadata"(): $Collection<($SlotMetadata)>
get "currentTooltip"(): $SlotMetadata<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonListSlot$Type = ($ButtonListSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonListSlot_ = $ButtonListSlot$Type;
}}
declare module "packages/journeymap/client/ui/component/$FloatSliderButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$IConfigFieldHolder, $IConfigFieldHolder$Type} from "packages/journeymap/client/ui/component/$IConfigFieldHolder"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FloatField, $FloatField$Type} from "packages/journeymap/common/properties/config/$FloatField"
import {$SliderButton, $SliderButton$Type} from "packages/journeymap/client/ui/component/$SliderButton"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $FloatSliderButton extends $Button implements $IConfigFieldHolder<($FloatField)>, $SliderButton {
 "prefix": string
 "dragging": boolean
 "minValue": float
 "maxValue": float
 "suffix": string
 "drawString": boolean
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $FloatField$Type, arg1: string, arg2: string, arg3: float, arg4: float, arg5: float, arg6: integer)
constructor(arg0: $FloatField$Type, arg1: string, arg2: string, arg3: float, arg4: float)
constructor(arg0: $FloatField$Type, arg1: string, arg2: string)

public "getValue"(): float
public "setValue"(arg0: float): void
public "refresh"(): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getConfigField"(): $FloatField
public "updateLabel"(): void
public "getFitWidth"(arg0: $Font$Type): integer
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "setSliderValue"(arg0: double): void
public "getSliderValue"(): double
get "value"(): float
set "value"(value: float)
get "configField"(): $FloatField
set "sliderValue"(value: double)
get "sliderValue"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatSliderButton$Type = ($FloatSliderButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatSliderButton_ = $FloatSliderButton$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ForgeEventHandlerManager$EventHandler {

}

export namespace $ForgeEventHandlerManager$EventHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventHandlerManager$EventHandler$Type = ($ForgeEventHandlerManager$EventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventHandlerManager$EventHandler_ = $ForgeEventHandlerManager$EventHandler$Type;
}}
declare module "packages/journeymap/client/event/handlers/$WorldEventHandler" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"

export class $WorldEventHandler {

constructor()

public "onUnload"(arg0: $LevelAccessor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventHandler$Type = ($WorldEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventHandler_ = $WorldEventHandler$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemePresets" {
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ThemePresets {
static "DEFAULT_DIRECTORY": string

constructor()

public static "getDefault"(): $Theme
public static "getPresetDirs"(): $List<(string)>
public static "getPresets"(): $List<($Theme)>
get "default"(): $Theme
get "presetDirs"(): $List<(string)>
get "presets"(): $List<($Theme)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemePresets$Type = ($ThemePresets);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemePresets_ = $ThemePresets$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$AddonOptionsManager" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$OptionsManager, $OptionsManager$Type} from "packages/journeymap/client/ui/dialog/$OptionsManager"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AddonOptionsManager extends $OptionsManager {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type)

public "m_7856_"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddonOptionsManager$Type = ($AddonOptionsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddonOptionsManager_ = $AddonOptionsManager$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawUtil" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Theme$LabelSpec, $Theme$LabelSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$LabelSpec"
import {$DrawUtil$VAlign, $DrawUtil$VAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$VAlign"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ShapeProperties, $ShapeProperties$Type} from "packages/journeymap/client/api/model/$ShapeProperties"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$DrawUtil$HAlign, $DrawUtil$HAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$HAlign"

export class $DrawUtil {
static "zLevel": double

constructor()

public static "drawColoredImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: float, arg7: double): void
public static "drawColoredImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: double): void
public static "drawColoredImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: integer, arg7: integer, arg8: double): void
public static "drawBatchLabel"(arg0: $PoseStack$Type, arg1: $Component$Type, arg2: $MultiBufferSource$Type, arg3: double, arg4: double, arg5: $DrawUtil$HAlign$Type, arg6: $DrawUtil$VAlign$Type, arg7: integer, arg8: float, arg9: integer, arg10: float, arg11: double, arg12: boolean): void
public static "drawBatchLabel"(arg0: $PoseStack$Type, arg1: $Component$Type, arg2: $MultiBufferSource$Type, arg3: $Theme$LabelSpec$Type, arg4: double, arg5: double, arg6: $DrawUtil$HAlign$Type, arg7: $DrawUtil$VAlign$Type, arg8: double, arg9: double): void
public static "drawBatchLabel"(arg0: $PoseStack$Type, arg1: $Component$Type, arg2: $MultiBufferSource$Type, arg3: double, arg4: double, arg5: $DrawUtil$HAlign$Type, arg6: $DrawUtil$VAlign$Type, arg7: integer, arg8: float, arg9: integer, arg10: float, arg11: double, arg12: boolean, arg13: double): void
public static "addVertexUV"(arg0: $PoseStack$Type, arg1: $BufferBuilder$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: (integer)[]): void
public static "drawBoundTexture"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double): void
public static "drawLabels"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: (string)[], arg3: double, arg4: double, arg5: $DrawUtil$HAlign$Type, arg6: $DrawUtil$VAlign$Type, arg7: integer, arg8: float, arg9: integer, arg10: float, arg11: double, arg12: boolean, arg13: double): void
public static "drawImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: float, arg3: double, arg4: double, arg5: boolean, arg6: float, arg7: double): void
public static "drawImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: double, arg3: double, arg4: boolean, arg5: float, arg6: double): void
public static "drawPolygon"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: $List$Type<($Point2D$Double$Type)>, arg4: $List$Type<($List$Type<($Point2D$Double$Type)>)>, arg5: $ShapeProperties$Type, arg6: integer): void
public static "drawColoredEntity"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: $Texture$Type, arg4: integer, arg5: float, arg6: float, arg7: double): void
public static "drawQuad"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: double): void
public static "drawQuad"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: double, arg7: double, arg8: boolean, arg9: double): void
public static "drawQuad"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: boolean, arg14: boolean, arg15: integer, arg16: integer, arg17: boolean): void
public static "getLabelHeight"(arg0: $Font$Type, arg1: boolean): integer
public static "drawWaypointIcon"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: float, arg3: integer, arg4: float, arg5: double, arg6: double, arg7: double): void
public static "drawColoredSprite"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: integer, arg9: float, arg10: double, arg11: double, arg12: float, arg13: double): void
public static "addVertexWithUV"(arg0: $PoseStack$Type, arg1: $BufferBuilder$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): void
public static "addVertexWithUV"(arg0: $PoseStack$Type, arg1: $BufferBuilder$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): void
public static "drawCenteredLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: double, arg3: double, arg4: integer, arg5: float, arg6: integer, arg7: float, arg8: double, arg9: boolean): void
public static "drawCenteredLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: double, arg3: double, arg4: integer, arg5: float, arg6: integer, arg7: float, arg8: double, arg9: double): void
public static "drawCenteredLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: double, arg3: double, arg4: integer, arg5: float, arg6: integer, arg7: float, arg8: double): void
public static "drawClampedImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: float, arg7: double): void
public static "drawClampedImage"(arg0: $PoseStack$Type, arg1: $Texture$Type, arg2: double, arg3: double, arg4: float, arg5: double): void
public static "drawEntity"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: double, arg4: $Texture$Type, arg5: float, arg6: float, arg7: double): void
public static "drawEntity"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: double, arg4: $Texture$Type, arg5: float, arg6: double): void
public static "sizeDisplay"(arg0: $PoseStack$Type, arg1: double, arg2: double): void
public static "drawLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: $Theme$LabelSpec$Type, arg3: double, arg4: double, arg5: $DrawUtil$HAlign$Type, arg6: $DrawUtil$VAlign$Type, arg7: double, arg8: double): void
public static "drawLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: double, arg3: double, arg4: $DrawUtil$HAlign$Type, arg5: $DrawUtil$VAlign$Type, arg6: integer, arg7: float, arg8: double, arg9: double, arg10: integer, arg11: float, arg12: double, arg13: boolean, arg14: double): void
public static "drawLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: double, arg3: double, arg4: $DrawUtil$HAlign$Type, arg5: $DrawUtil$VAlign$Type, arg6: integer, arg7: float, arg8: integer, arg9: float, arg10: double, arg11: boolean): void
public static "drawLabel"(arg0: $GuiGraphics$Type, arg1: string, arg2: double, arg3: double, arg4: $DrawUtil$HAlign$Type, arg5: $DrawUtil$VAlign$Type, arg6: integer, arg7: float, arg8: integer, arg9: float, arg10: double, arg11: boolean, arg12: double): void
public static "drawRectangle"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: integer, arg6: float): void
public static "drawGradientRect"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: integer, arg6: float, arg7: integer, arg8: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawUtil$Type = ($DrawUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawUtil_ = $DrawUtil$Type;
}}
declare module "packages/journeymap/client/properties/$MapProperties" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$ClientPropertiesBase, $ClientPropertiesBase$Type} from "packages/journeymap/client/properties/$ClientPropertiesBase"

export class $MapProperties extends $ClientPropertiesBase implements $Comparable<($MapProperties)> {
readonly "showWaypoints": $BooleanField
readonly "showSelf": $BooleanField
readonly "showGrid": $BooleanField
readonly "showCaves": $BooleanField
readonly "showEntityNames": $BooleanField
readonly "preferredMapType": $EnumField<($MapType$Name)>
readonly "zoomLevel": $IntegerField

constructor()

public "compareTo"(arg0: $MapProperties$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapProperties$Type = ($MapProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapProperties_ = $MapProperties$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$WaypointManager" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$WaypointManagerItem, $WaypointManagerItem$Type} from "packages/journeymap/client/ui/waypoint/$WaypointManagerItem"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointManager extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Waypoint$Type, arg1: $JmUI$Type)
constructor(arg0: $JmUI$Type)
constructor()

public "setFocusWaypoint"(arg0: $Waypoint$Type): void
public "removeWaypoint"(arg0: $WaypointManagerItem$Type): void
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "m_7856_"(): void
public static "toggleAllWaypoints"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
set "focusWaypoint"(value: $Waypoint$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointManager$Type = ($WaypointManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointManager_ = $WaypointManager$Type;
}}
declare module "packages/journeymap/client/api/option/$CustomIntegerOption" {
import {$CustomOption, $CustomOption$Type} from "packages/journeymap/client/api/option/$CustomOption"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $CustomIntegerOption extends $CustomOption<(integer)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: integer, arg4: integer, arg5: integer, arg6: boolean)

public "getAllowNeg"(): boolean
public "getMinValue"(): integer
public "getMaxValue"(): integer
get "allowNeg"(): boolean
get "minValue"(): integer
get "maxValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomIntegerOption$Type = ($CustomIntegerOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomIntegerOption_ = $CustomIntegerOption$Type;
}}
declare module "packages/journeymap/client/api/display/$IOverlayListener" {
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export interface $IOverlayListener {

 "onMouseClick"(arg0: $UIState$Type, arg1: $Point2D$Double$Type, arg2: $BlockPos$Type, arg3: integer, arg4: boolean): boolean
 "onMouseMove"(arg0: $UIState$Type, arg1: $Point2D$Double$Type, arg2: $BlockPos$Type): void
 "onDeactivate"(arg0: $UIState$Type): void
 "onActivate"(arg0: $UIState$Type): void
 "onOverlayMenuPopup"(arg0: $UIState$Type, arg1: $Point2D$Double$Type, arg2: $BlockPos$Type, arg3: $ModPopupMenu$Type): void
 "onMouseOut"(arg0: $UIState$Type, arg1: $Point2D$Double$Type, arg2: $BlockPos$Type): void
}

export namespace $IOverlayListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IOverlayListener$Type = ($IOverlayListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IOverlayListener_ = $IOverlayListener$Type;
}}
declare module "packages/journeymap/client/log/$ChatLog" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$File, $File$Type} from "packages/java/io/$File"

export class $ChatLog {
static "enableAnnounceMod": boolean

constructor()

public static "announceMod"(arg0: boolean): void
public static "announceError"(arg0: string): void
public static "announceI18N"(arg0: string, ...arg1: (any)[]): void
public static "announceURL"(arg0: string, arg1: string): void
public static "queueAnnouncement"(arg0: $Component$Type): void
public static "announceFile"(arg0: string, arg1: $File$Type): void
public static "showChatAnnouncements"(arg0: $Minecraft$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatLog$Type = ($ChatLog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatLog_ = $ChatLog$Type;
}}
declare module "packages/journeymap/client/task/main/$ExpireTextureTask" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"

export class $ExpireTextureTask implements $IMainThreadTask {


public "getName"(): string
public static "queue"(arg0: integer): void
public static "queue"(arg0: $Texture$Type): void
public static "queue"(arg0: $Collection$Type<($Texture$Type)>): void
public "perform"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type): $IMainThreadTask
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpireTextureTask$Type = ($ExpireTextureTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpireTextureTask_ = $ExpireTextureTask$Type;
}}
declare module "packages/journeymap/client/api/impl/$ModPopupMenuImpl" {
import {$ModPopupMenu$Action, $ModPopupMenu$Action$Type} from "packages/journeymap/client/api/display/$ModPopupMenu$Action"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ModPopupMenuImpl$MenuItem, $ModPopupMenuImpl$MenuItem$Type} from "packages/journeymap/client/api/impl/$ModPopupMenuImpl$MenuItem"
import {$PopupMenu, $PopupMenu$Type} from "packages/journeymap/client/ui/fullscreen/menu/$PopupMenu"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $ModPopupMenuImpl implements $ModPopupMenu {

constructor(arg0: $PopupMenu$Type)
constructor(arg0: $PopupMenu$Type, arg1: boolean)

public "getMenuItemList"(): $List<($ModPopupMenuImpl$MenuItem)>
public "isSub"(): boolean
public "createSubItemList"(arg0: string): $ModPopupMenu
public "addMenuItem"(arg0: string, arg1: $ModPopupMenu$Action$Type): $ModPopupMenu
public "addMenuItemScreen"(arg0: string, arg1: $Screen$Type): $ModPopupMenu
get "menuItemList"(): $List<($ModPopupMenuImpl$MenuItem)>
get "sub"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModPopupMenuImpl$Type = ($ModPopupMenuImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModPopupMenuImpl_ = $ModPopupMenuImpl$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$Orientation" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $Orientation extends $Enum<($Orientation)> implements $KeyedEnum {
static readonly "North": $Orientation
static readonly "OldNorth": $Orientation
static readonly "PlayerHeading": $Orientation
readonly "key": string


public "toString"(): string
public static "values"(): ($Orientation)[]
public static "valueOf"(arg0: string): $Orientation
public "getKey"(): string
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Orientation$Type = (("playerheading") | ("oldnorth") | ("north")) | ($Orientation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Orientation_ = $Orientation$Type;
}}
declare module "packages/journeymap/client/render/ingame/$WaypointRenderer" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"

export class $WaypointRenderer {


public "render"(arg0: $PoseStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointRenderer$Type = ($WaypointRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointRenderer_ = $WaypointRenderer$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$LogKt" {
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $LogKt {


public static "logGet"(arg0: $RouteHandler$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogKt$Type = ($LogKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogKt_ = $LogKt$Type;
}}
declare module "packages/journeymap/client/render/draw/$MatrixDrawUtil" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$DrawUtil$HAlign, $DrawUtil$HAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$HAlign"
import {$DrawUtil$VAlign, $DrawUtil$VAlign$Type} from "packages/journeymap/client/render/draw/$DrawUtil$VAlign"

export class $MatrixDrawUtil {
static "zLevel": integer

constructor()

public static "fill"(arg0: $Matrix4f$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "drawColoredImage"(arg0: $Texture$Type, arg1: $PoseStack$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: float, arg5: double, arg6: double, arg7: double): void
public static "addVertexUV"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type, arg2: $VertexConsumer$Type, arg3: float, arg4: float, arg5: float, arg6: float, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public static "addVertex"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type, arg2: $VertexConsumer$Type, arg3: float, arg4: float, arg5: float, arg6: float, arg7: double, arg8: double, arg9: float): void
public static "drawQuad"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: double, arg7: double, arg8: boolean, arg9: double): void
public static "drawQuad"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: float, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: boolean, arg14: boolean, arg15: integer, arg16: integer, arg17: boolean): void
public static "addVertexUVOverlay"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type, arg2: $VertexConsumer$Type, arg3: float, arg4: float, arg5: float, arg6: float, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float, arg12: integer): void
public static "drawLabel"(arg0: string, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: double, arg4: double, arg5: $DrawUtil$HAlign$Type, arg6: $DrawUtil$VAlign$Type, arg7: integer, arg8: float, arg9: integer, arg10: float, arg11: double, arg12: boolean): void
public static "drawRectangle"(arg0: $Matrix4f$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: integer): void
public static "addBufferedVertexWithUV"(arg0: $BufferBuilder$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatrixDrawUtil$Type = ($MatrixDrawUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatrixDrawUtil_ = $MatrixDrawUtil$Type;
}}
declare module "packages/journeymap/client/api/model/$TextProperties" {
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $TextProperties {

constructor()

public "toString"(): string
public "setScale"(arg0: float): $TextProperties
public "setColor"(arg0: integer): $TextProperties
public "setOpacity"(arg0: float): $TextProperties
public "getBackgroundColor"(): integer
public "getScale"(): float
public "setBackgroundColor"(arg0: integer): $TextProperties
public "isActiveIn"(arg0: $UIState$Type): boolean
public "setFontShadow"(arg0: boolean): $TextProperties
public "getMinZoom"(): integer
public "setMinZoom"(arg0: integer): $TextProperties
public "getActiveMapTypes"(): $EnumSet<($Context$MapType)>
public "getMaxZoom"(): integer
public "setMaxZoom"(arg0: integer): $TextProperties
public "getActiveUIs"(): $EnumSet<($Context$UI)>
public "getColor"(): integer
public "setActiveUIs"(arg0: $EnumSet$Type<($Context$UI$Type)>): $TextProperties
public "setActiveMapTypes"(arg0: $EnumSet$Type<($Context$MapType$Type)>): $TextProperties
public "getOffsetX"(): integer
public "getOffsetY"(): integer
public "getOpacity"(): float
public "setBackgroundOpacity"(arg0: float): $TextProperties
public "setOffsetX"(arg0: integer): $TextProperties
public "setOffsetY"(arg0: integer): $TextProperties
public "hasFontShadow"(): boolean
public "getBackgroundOpacity"(): float
set "scale"(value: float)
set "color"(value: integer)
set "opacity"(value: float)
get "backgroundColor"(): integer
get "scale"(): float
set "backgroundColor"(value: integer)
set "fontShadow"(value: boolean)
get "minZoom"(): integer
set "minZoom"(value: integer)
get "activeMapTypes"(): $EnumSet<($Context$MapType)>
get "maxZoom"(): integer
set "maxZoom"(value: integer)
get "activeUIs"(): $EnumSet<($Context$UI)>
get "color"(): integer
set "activeUIs"(value: $EnumSet$Type<($Context$UI$Type)>)
set "activeMapTypes"(value: $EnumSet$Type<($Context$MapType$Type)>)
get "offsetX"(): integer
get "offsetY"(): integer
get "opacity"(): float
set "backgroundOpacity"(value: float)
set "offsetX"(value: integer)
set "offsetY"(value: integer)
get "backgroundOpacity"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextProperties$Type = ($TextProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextProperties_ = $TextProperties$Type;
}}
declare module "packages/journeymap/client/task/multi/$TaskBatch" {
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"

export class $TaskBatch implements $ITask {

constructor(arg0: $List$Type<($ITask$Type)>)

public "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "getMaxRuntime"(): integer
get "maxRuntime"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TaskBatch$Type = ($TaskBatch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TaskBatch_ = $TaskBatch$Type;
}}
declare module "packages/journeymap/client/io/$FileHandler" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Properties, $Properties$Type} from "packages/java/util/$Properties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $FileHandler {
static readonly "DEV_MINECRAFT_DIR": string
static readonly "ASSETS_JOURNEYMAP": string
static readonly "ASSETS_JOURNEYMAP_UI": string
static readonly "ASSETS_WEBMAP": string
static readonly "MinecraftDirectory": $File
static readonly "JourneyMapDirectory": $File
static readonly "StandardConfigDirectory": $File

constructor()

public static "delete"(arg0: $File$Type): boolean
public static "open"(arg0: $File$Type): void
public static "getMessageModelInputStream"(arg0: string, arg1: string): $InputStream
public static "copyColorPaletteHtmlFile"(arg0: $File$Type, arg1: string): $File
public static "getWorldDirectoryName"(arg0: $Minecraft$Type, arg1: string): string
public static "getWorldDirectoryName"(arg0: $Minecraft$Type): string
public static "getJMWorldDir"(arg0: $Minecraft$Type, arg1: string): $File
public static "getJMWorldDir"(arg0: $Minecraft$Type): $File
public static "getDimNameForPath"(arg0: $File$Type, arg1: $ResourceKey$Type<($Level$Type)>): string
public static "copyResources"(arg0: $File$Type, arg1: $URL$Type, arg2: string, arg3: string, arg4: boolean): boolean
public static "copyResources"(arg0: $File$Type, arg1: $ResourceLocation$Type, arg2: string, arg3: boolean): boolean
public static "getWorldConfigDir"(arg0: boolean): $File
public static "getJMWorldDirForWorldId"(arg0: $Minecraft$Type, arg1: string): $File
public static "getJourneyMapDir"(): $File
public static "getDimPath"(arg0: $File$Type, arg1: $ResourceKey$Type<($Level$Type)>): $Path
public static "getWorldSaveDir"(arg0: $Minecraft$Type): $File
public static "getWaypointDir"(): $File
public static "getWaypointDir"(arg0: $File$Type): $File
public static "getMCWorldDir"(arg0: $Minecraft$Type, arg1: $ResourceKey$Type<($Level$Type)>): $File
public static "getMCWorldDir"(arg0: $Minecraft$Type): $File
public static "getLangFile"(arg0: string): $Properties
public static "getMessageModel"<M>(arg0: $Class$Type<(M)>, arg1: string): M
public static "isInJar"(): boolean
public static "isInJar"(arg0: $URL$Type): boolean
public static "getAddonDataPath"(arg0: $Minecraft$Type): $File
public static "getIconFromFile"(arg0: $File$Type, arg1: string, arg2: string): $NativeImage
public static "getMinecraftDirectory"(): $File
get "journeyMapDir"(): $File
get "waypointDir"(): $File
get "inJar"(): boolean
get "minecraftDirectory"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileHandler$Type = ($FileHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileHandler_ = $FileHandler$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/$Fullscreen" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MapState, $MapState$Type} from "packages/journeymap/client/model/$MapState"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"
import {$PopupMenu, $PopupMenu$Type} from "packages/journeymap/client/ui/fullscreen/menu/$PopupMenu"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $Fullscreen extends $JmUI implements $IFullscreen {
 "chatOpenedFromEvent": boolean
readonly "popupMenu": $PopupMenu
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor()

public "close"(): void
public "reset"(): void
public static "state"(): $MapState
public "init"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "m_7856_"(): void
public "createWaypointAtMouse"(): void
public "chatPositionAtMouse"(): void
public "removed"(): void
public "isChatOpen"(): boolean
public "moveCanvas"(arg0: integer, arg1: integer): void
public "hideButtons"(): void
public "isSearchFocused"(): boolean
public "zoomIn"(): void
public "zoomOut"(): void
public "toggleMapType"(): void
public "getScreen"(): $Screen
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public static "uiState"(): $UIState
public "getMapType"(): $MapType
public "getUiState"(): $UIState
public "queueToolTip"(arg0: $List$Type<($Component$Type)>): void
public "updateMapType"(arg0: $Context$MapType$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>): void
public "isPauseScreen"(): boolean
public "resize"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): void
public "tick"(): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "getBlockAtMouse"(): $BlockPos
public "toggleSearchBar"(arg0: boolean): void
public "openChat"(arg0: string): void
public "centerOn"(arg0: $Waypoint$Type): void
public "centerOn"(arg0: double, arg1: double): void
public "addTempMarker"(arg0: $Waypoint$Type): void
public "setTheme"(arg0: string): void
public "addButtonWidget"(arg0: $Button$Type): void
public "showCaveLayers"(): void
public "getMenuToolbarBounds"(): $Rectangle2D$Double
public "getOptionsToolbarBounds"(): $Rectangle2D$Double
public "getMinecraft"(): $Minecraft
get "chatOpen"(): boolean
get "searchFocused"(): boolean
get "screen"(): $Screen
get "mapType"(): $MapType
get "pauseScreen"(): boolean
get "blockAtMouse"(): $BlockPos
set "theme"(value: string)
get "menuToolbarBounds"(): $Rectangle2D$Double
get "optionsToolbarBounds"(): $Rectangle2D$Double
get "minecraft"(): $Minecraft
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Fullscreen$Type = ($Fullscreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Fullscreen_ = $Fullscreen$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$AutoMapConfirmation" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AutoMapConfirmation extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor()
constructor(arg0: $JmUI$Type)

public "m_7856_"(): void
public "charTyped"(arg0: character, arg1: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoMapConfirmation$Type = ($AutoMapConfirmation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoMapConfirmation_ = $AutoMapConfirmation$Type;
}}
declare module "packages/journeymap/client/cartography/color/$RGB" {
import {$Color, $Color$Type} from "packages/java/awt/$Color"

export class $RGB {
static readonly "ALPHA_OPAQUE": integer
static readonly "BLACK_ARGB": integer
static readonly "BLACK_RGB": integer
static readonly "WHITE_ARGB": integer
static readonly "WHITE_RGB": integer
static readonly "GREEN_RGB": integer
static readonly "RED_RGB": integer
static readonly "BLUE_RGB": integer
static readonly "CYAN_RGB": integer
static readonly "GRAY_RGB": integer
static readonly "DARK_GRAY_RGB": integer
static readonly "LIGHT_GRAY_RGB": integer


public static "toString"(arg0: integer): string
public static "max"(...arg0: (integer)[]): integer
public static "toHexString"(arg0: integer): string
public static "multiply"(arg0: integer, arg1: integer): integer
public static "subtract"(arg0: integer, arg1: integer): integer
public static "ints"(arg0: integer, arg1: float): (integer)[]
public static "ints"(arg0: integer): (integer)[]
public static "ints"(arg0: integer, arg1: integer): (integer)[]
public static "toRgba"(arg0: integer, arg1: float): integer
public static "adjustBrightness"(arg0: integer, arg1: float): integer
public static "clampFloats"(arg0: (float)[], arg1: float): (float)[]
public static "greyScale"(arg0: integer): integer
public static "toArbg"(arg0: integer, arg1: float): integer
public static "clampFloat"(arg0: float): float
public static "darkenAmbient"(arg0: integer, arg1: float, arg2: (float)[]): integer
public static "blendWith"(arg0: integer, arg1: integer, arg2: float): integer
public static "clampInt"(arg0: integer): integer
public static "randomColor"(): integer
public static "toClampedInt"(arg0: float): integer
public static "bevelSlope"(arg0: integer, arg1: float): integer
public static "toScaledFloat"(arg0: integer): float
public static "tint"(arg0: integer, arg1: integer): integer
public static "toInteger"(arg0: (float)[]): integer
public static "toInteger"(arg0: integer, arg1: integer, arg2: integer): integer
public static "toInteger"(arg0: float, arg1: float, arg2: float): integer
public static "toInteger"(arg0: (integer)[]): integer
public static "isWhite"(arg0: integer): boolean
public static "rgbaToRgb"(arg0: integer): integer
public static "isBlack"(arg0: integer): boolean
public static "floats"(arg0: integer, arg1: float): (float)[]
public static "floats"(arg0: integer): (float)[]
public static "hexToInt"(arg0: string): integer
public static "toColor"(arg0: integer): $Color
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RGB$Type = ($RGB);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RGB_ = $RGB$Type;
}}
declare module "packages/journeymap/client/event/dispatchers/$CustomEventDispatcher" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$ThemeToolbar, $ThemeToolbar$Type} from "packages/journeymap/client/ui/theme/$ThemeToolbar"
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$EntityRadarUpdateEvent$EntityType, $EntityRadarUpdateEvent$EntityType$Type} from "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent$EntityType"
import {$EventDispatcher, $EventDispatcher$Type} from "packages/journeymap/client/event/dispatchers/$EventDispatcher"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $CustomEventDispatcher {


public static "getInstance"(): $CustomEventDispatcher
public static "init"(arg0: $EventDispatcher$Type): void
public "popupMenuEvent"(arg0: $Fullscreen$Type, arg1: $ModPopupMenu$Type): boolean
public "getMapTypeToolbar"(arg0: $Fullscreen$Type, arg1: $Theme$Type, ...arg2: ($Button$Type)[]): $ThemeToolbar
public "getCustomToolBars"(arg0: $Fullscreen$Type, arg1: $Theme$Type): $List<($ThemeToolbar)>
public "getAddonToolbar"(arg0: $Fullscreen$Type, arg1: $Theme$Type): $ThemeToolbar
public "popupWaypointMenuEvent"(arg0: $Fullscreen$Type, arg1: $ModPopupMenu$Type, arg2: $Waypoint$Type): boolean
public "entityRadarUpdateEvent"(arg0: $EntityRadarUpdateEvent$EntityType$Type, arg1: $EntityDTO$Type): boolean
get "instance"(): $CustomEventDispatcher
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomEventDispatcher$Type = ($CustomEventDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomEventDispatcher_ = $CustomEventDispatcher$Type;
}}
declare module "packages/journeymap/client/model/$SplashInfo$Line" {
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"

export class $SplashInfo$Line {
 "label": string
 "action": string

constructor()
constructor(arg0: string, arg1: string)

public "invokeAction"(arg0: $JmUI$Type): void
public "hasAction"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SplashInfo$Line$Type = ($SplashInfo$Line);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SplashInfo$Line_ = $SplashInfo$Line$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$DeleteMapConfirmation" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DeleteMapConfirmation extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor()
constructor(arg0: $JmUI$Type)

public "m_7856_"(): void
public "charTyped"(arg0: character, arg1: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeleteMapConfirmation$Type = ($DeleteMapConfirmation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeleteMapConfirmation_ = $DeleteMapConfirmation$Type;
}}
declare module "packages/journeymap/client/mod/$ModBlockDelegate" {
import {$IBlockSpritesProxy, $IBlockSpritesProxy$Type} from "packages/journeymap/client/mod/$IBlockSpritesProxy"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"

export class $ModBlockDelegate extends $Enum<($ModBlockDelegate)> {
static readonly "INSTANCE": $ModBlockDelegate


public static "values"(): ($ModBlockDelegate)[]
public static "valueOf"(arg0: string): $ModBlockDelegate
public "initialize"(arg0: $BlockMD$Type): void
public "reset"(): void
public "getCommonBlockHandler"(): $IModBlockHandler
public "getDefaultBlockColorProxy"(): $IBlockColorProxy
public "getMaterialBlockColorProxy"(): $IBlockColorProxy
public "getDefaultBlockSpritesProxy"(): $IBlockSpritesProxy
get "commonBlockHandler"(): $IModBlockHandler
get "defaultBlockColorProxy"(): $IBlockColorProxy
get "materialBlockColorProxy"(): $IBlockColorProxy
get "defaultBlockSpritesProxy"(): $IBlockSpritesProxy
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlockDelegate$Type = (("instance")) | ($ModBlockDelegate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlockDelegate_ = $ModBlockDelegate$Type;
}}
declare module "packages/journeymap/client/api/option/$Option" {
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $Option<T> {


public "get"(): T
public "set"(arg0: T): void
public "getDefaultValue"(): T
public "getFieldName"(): string
public "getCategory"(): $OptionCategory
public "getLabel"(): string
public "setSortOrder"(arg0: integer): $Option<(T)>
public "getSortOrder"(): integer
get "defaultValue"(): T
get "fieldName"(): string
get "category"(): $OptionCategory
get "label"(): string
set "sortOrder"(value: integer)
get "sortOrder"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Option$Type<T> = ($Option<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Option_<T> = $Option$Type<(T)>;
}}
declare module "packages/journeymap/client/task/multi/$ITask" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$File, $File$Type} from "packages/java/io/$File"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"

export interface $ITask {

 "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
 "getMaxRuntime"(): integer
}

export namespace $ITask {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITask$Type = ($ITask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITask_ = $ITask$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Container" {
import {$Theme$Container$Toolbar, $Theme$Container$Toolbar$Type} from "packages/journeymap/client/ui/theme/$Theme$Container$Toolbar"

export class $Theme$Container {
 "toolbar": $Theme$Container$Toolbar

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Container$Type = ($Theme$Container);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Container_ = $Theme$Container$Type;
}}
declare module "packages/journeymap/client/render/ingame/$WaypointRenderTypes" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$TextureStateShard, $RenderStateShard$TextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TextureStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$RenderType$CompositeRenderType, $RenderType$CompositeRenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType$CompositeRenderType"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ColorLogicStateShard, $RenderStateShard$ColorLogicStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ColorLogicStateShard"
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$DrawBuffer, $DrawBuffer$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$DrawBuffer"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"

export class $WaypointRenderTypes extends $RenderType {
static readonly "BEAM_RENDER_TYPE": $RenderType
static readonly "BIG_BUFFER_SIZE": integer
static readonly "MEDIUM_BUFFER_SIZE": integer
static readonly "SMALL_BUFFER_SIZE": integer
static readonly "TRANSIENT_BUFFER_SIZE": integer
static readonly "LINES": $RenderType$CompositeRenderType
static readonly "LINE_STRIP": $RenderType$CompositeRenderType
 "sortOnUpload": boolean
static readonly "VIEW_SCALE_Z_EPSILON": float
static readonly "MAX_ENCHANTMENT_GLINT_SPEED_MILLIS": double
readonly "name": string
 "setupState": $Runnable
static readonly "NO_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "ADDITIVE_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "LIGHTNING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "GLINT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "CRUMBLING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "TRANSLUCENT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "NO_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_MIPPED_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_MOVING_BLOCK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_NO_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_BEACON_BEAM_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_DECAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_NO_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SHADOW_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_ALPHA_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_EYES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENERGY_SWIRL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LEASH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_WATER_MASK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LIGHTNING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRIPWIRE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_PORTAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_GATEWAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LINES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "BLOCK_SHEET_MIPPED": $RenderStateShard$TextureStateShard
static readonly "BLOCK_SHEET": $RenderStateShard$TextureStateShard
static readonly "NO_TEXTURE": $RenderStateShard$EmptyTextureStateShard
static readonly "DEFAULT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "ENTITY_GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "NO_LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "NO_OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "CULL": $RenderStateShard$CullStateShard
static readonly "NO_CULL": $RenderStateShard$CullStateShard
static readonly "NO_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "EQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "LEQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "GREATER_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "COLOR_DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "COLOR_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "NO_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "POLYGON_OFFSET_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "VIEW_OFFSET_Z_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "MAIN_TARGET": $RenderStateShard$OutputStateShard
static readonly "OUTLINE_TARGET": $RenderStateShard$OutputStateShard
static readonly "TRANSLUCENT_TARGET": $RenderStateShard$OutputStateShard
static readonly "PARTICLES_TARGET": $RenderStateShard$OutputStateShard
static readonly "WEATHER_TARGET": $RenderStateShard$OutputStateShard
static readonly "CLOUDS_TARGET": $RenderStateShard$OutputStateShard
static readonly "ITEM_ENTITY_TARGET": $RenderStateShard$OutputStateShard
static readonly "DEFAULT_LINE": $RenderStateShard$LineStateShard
static readonly "NO_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard
static readonly "OR_REVERSE_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard

constructor(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: integer, arg4: boolean, arg5: boolean, arg6: $Runnable$Type, arg7: $Runnable$Type)

public static "getIcon"(arg0: $Texture$Type): $RenderType
public static "getDrawBuffer"(arg0: $RenderType$Type): $DrawBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointRenderTypes$Type = ($WaypointRenderTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointRenderTypes_ = $WaypointRenderTypes$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeHudOverlayEvents" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$RenderGuiOverlayEvent$Pre, $RenderGuiOverlayEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderGuiOverlayEvent$Pre"
import {$RenderGuiOverlayEvent$Post, $RenderGuiOverlayEvent$Post$Type} from "packages/net/minecraftforge/client/event/$RenderGuiOverlayEvent$Post"
import {$RenderGuiEvent$Pre, $RenderGuiEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderGuiEvent$Pre"
import {$CustomizeGuiOverlayEvent$DebugText, $CustomizeGuiOverlayEvent$DebugText$Type} from "packages/net/minecraftforge/client/event/$CustomizeGuiOverlayEvent$DebugText"

export class $ForgeHudOverlayEvents implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "preOverlay"(arg0: $RenderGuiEvent$Pre$Type): void
public "preOverlayLow"(arg0: $RenderGuiOverlayEvent$Pre$Type): void
public "postOverlay"(arg0: $RenderGuiOverlayEvent$Post$Type): void
public "onRenderOverlayDebug"(arg0: $CustomizeGuiOverlayEvent$DebugText$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeHudOverlayEvents$Type = ($ForgeHudOverlayEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeHudOverlayEvents_ = $ForgeHudOverlayEvents$Type;
}}
declare module "packages/journeymap/client/command/$ClientCommandInvoker" {
import {$JMCommand, $JMCommand$Type} from "packages/journeymap/client/command/$JMCommand"
import {$CommandSource, $CommandSource$Type} from "packages/net/minecraft/commands/$CommandSource"

export class $ClientCommandInvoker implements $JMCommand {

constructor()

public "getName"(): string
public "execute"(arg0: $CommandSource$Type, arg1: (string)[]): integer
public "registerSub"(arg0: $JMCommand$Type): $ClientCommandInvoker
public "getSubCommand"(arg0: (string)[]): $JMCommand
public "getPossibleCommands"(): string
public "getUsage"(arg0: $CommandSource$Type): string
get "name"(): string
get "possibleCommands"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCommandInvoker$Type = ($ClientCommandInvoker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCommandInvoker_ = $ClientCommandInvoker$Type;
}}
declare module "packages/journeymap/client/cartography/color/$ColorManager" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ColoredSprite, $ColoredSprite$Type} from "packages/journeymap/client/cartography/color/$ColoredSprite"
import {$ColorPalette, $ColorPalette$Type} from "packages/journeymap/client/cartography/color/$ColorPalette"

export class $ColorManager extends $Enum<($ColorManager)> {
static readonly "INSTANCE": $ColorManager


public static "values"(): ($ColorManager)[]
public static "valueOf"(arg0: string): $ColorManager
public "reset"(): void
public "ensureCurrent"(arg0: boolean): void
public "getCurrentPalette"(): $ColorPalette
public "getAverageColor"(arg0: $Collection$Type<($ColoredSprite$Type)>): (float)[]
public static "getResourcePackNames"(): string
get "currentPalette"(): $ColorPalette
get "resourcePackNames"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorManager$Type = (("instance")) | ($ColorManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorManager_ = $ColorManager$Type;
}}
declare module "packages/journeymap/client/texture/$ComparableNativeImage" {
import {$NativeImage$Format, $NativeImage$Format$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage$Format"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $ComparableNativeImage extends $NativeImage {
 "pixels": long

constructor(arg0: $NativeImage$Type)
constructor(arg0: $NativeImage$Format$Type, arg1: integer, arg2: integer)

public "close"(): void
public "isChanged"(): boolean
public "identicalTo"(arg0: $NativeImage$Type): boolean
public static "getPixelData"(arg0: $NativeImage$Type): (integer)[]
public "getPixelData"(): (integer)[]
public static "areIdentical"(arg0: (integer)[], arg1: (integer)[]): boolean
public "setChanged"(arg0: boolean): void
public "setPixelRGBA"(arg0: integer, arg1: integer, arg2: integer): void
get "changed"(): boolean
get "pixelData"(): (integer)[]
set "changed"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComparableNativeImage$Type = ($ComparableNativeImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComparableNativeImage_ = $ComparableNativeImage$Type;
}}
declare module "packages/journeymap/client/model/$GridSpec$Style" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $GridSpec$Style extends $Enum<($GridSpec$Style)> implements $KeyedEnum {
static readonly "Squares": $GridSpec$Style
static readonly "SquaresWithRegion": $GridSpec$Style
static readonly "GridRegion": $GridSpec$Style
static readonly "Dots": $GridSpec$Style
static readonly "Checkers": $GridSpec$Style


public static "values"(): ($GridSpec$Style)[]
public static "valueOf"(arg0: string): $GridSpec$Style
public "getKey"(): string
public "displayName"(): string
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridSpec$Style$Type = (("gridregion") | ("dots") | ("checkers") | ("squares") | ("squareswithregion")) | ($GridSpec$Style);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridSpec$Style_ = $GridSpec$Style$Type;
}}
declare module "packages/journeymap/client/io/nbt/$RegionLoader" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Stack, $Stack$Type} from "packages/java/util/$Stack"

export class $RegionLoader {

constructor(arg0: $Minecraft$Type, arg1: $MapType$Type, arg2: boolean)

public static "getRegionFile"(arg0: $Minecraft$Type, arg1: integer, arg2: integer): $File
public static "getRegionFile"(arg0: $Minecraft$Type, arg1: integer, arg2: integer, arg3: integer): $File
public "getMapType"(): $MapType
public "getRegions"(): $Stack<($RegionCoord)>
public "getRegionsFound"(): integer
public "getVSlice"(): integer
public "isUnderground"(): boolean
public "regionIterator"(): $Iterator<($RegionCoord)>
get "mapType"(): $MapType
get "regions"(): $Stack<($RegionCoord)>
get "regionsFound"(): integer
get "vSlice"(): integer
get "underground"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionLoader$Type = ($RegionLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionLoader_ = $RegionLoader$Type;
}}
declare module "packages/journeymap/client/api/display/$Overlay" {
import {$TextProperties, $TextProperties$Type} from "packages/journeymap/client/api/model/$TextProperties"
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$Displayable, $Displayable$Type} from "packages/journeymap/client/api/display/$Displayable"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"
import {$IOverlayListener, $IOverlayListener$Type} from "packages/journeymap/client/api/display/$IOverlayListener"

export class $Overlay extends $Displayable {


public "getDisplayOrder"(): integer
public "getDimension"(): $ResourceKey<($Level)>
public "setTitle"(arg0: string): $Overlay
public "isActiveIn"(arg0: $UIState$Type): boolean
public "getTitle"(): string
public "setLabel"(arg0: string): $Overlay
public "getMinZoom"(): integer
public "setMinZoom"(arg0: integer): $Overlay
public "getActiveMapTypes"(): $EnumSet<($Context$MapType)>
public "getMaxZoom"(): integer
public "setMaxZoom"(arg0: integer): $Overlay
public "getActiveUIs"(): $EnumSet<($Context$UI)>
public "setOverlayListener"(arg0: $IOverlayListener$Type): $Overlay
public "setTextProperties"(arg0: $TextProperties$Type): $Overlay
public "setDisplayOrder"(arg0: integer): $Overlay
public "getOverlayListener"(): $IOverlayListener
public "getTextProperties"(): $TextProperties
public "getNeedsRerender"(): boolean
public "setActiveUIs"(arg0: $EnumSet$Type<($Context$UI$Type)>): $Overlay
public "setActiveMapTypes"(arg0: $EnumSet$Type<($Context$MapType$Type)>): $Overlay
public "getLabel"(): string
public "flagForRerender"(): void
public "setDimension"(arg0: $ResourceKey$Type<($Level$Type)>): $Overlay
public "getOverlayGroupName"(): string
public "setOverlayGroupName"(arg0: string): $Overlay
public "clearFlagForRerender"(): void
get "displayOrder"(): integer
get "dimension"(): $ResourceKey<($Level)>
set "title"(value: string)
get "title"(): string
set "label"(value: string)
get "minZoom"(): integer
set "minZoom"(value: integer)
get "activeMapTypes"(): $EnumSet<($Context$MapType)>
get "maxZoom"(): integer
set "maxZoom"(value: integer)
get "activeUIs"(): $EnumSet<($Context$UI)>
set "overlayListener"(value: $IOverlayListener$Type)
set "textProperties"(value: $TextProperties$Type)
set "displayOrder"(value: integer)
get "overlayListener"(): $IOverlayListener
get "textProperties"(): $TextProperties
get "needsRerender"(): boolean
set "activeUIs"(value: $EnumSet$Type<($Context$UI$Type)>)
set "activeMapTypes"(value: $EnumSet$Type<($Context$MapType$Type)>)
get "label"(): string
set "dimension"(value: $ResourceKey$Type<($Level$Type)>)
get "overlayGroupName"(): string
set "overlayGroupName"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Overlay$Type = ($Overlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Overlay_ = $Overlay$Type;
}}
declare module "packages/journeymap/client/command/$CmdReloadWaypoint" {
import {$JMCommand, $JMCommand$Type} from "packages/journeymap/client/command/$JMCommand"
import {$CommandSource, $CommandSource$Type} from "packages/net/minecraft/commands/$CommandSource"

export class $CmdReloadWaypoint implements $JMCommand {

constructor()

public "getName"(): string
public "execute"(arg0: $CommandSource$Type, arg1: (string)[]): integer
public "getUsage"(arg0: $CommandSource$Type): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdReloadWaypoint$Type = ($CmdReloadWaypoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdReloadWaypoint_ = $CmdReloadWaypoint$Type;
}}
declare module "packages/journeymap/client/mod/impl/$TerraFirmaCraft" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $TerraFirmaCraft implements $IModBlockHandler, $IBlockColorProxy {

constructor()

public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public "initialize"(arg0: $BlockMD$Type): void
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TerraFirmaCraft$Type = ($TerraFirmaCraft);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TerraFirmaCraft_ = $TerraFirmaCraft$Type;
}}
declare module "packages/journeymap/client/api/display/$Waypoint" {
import {$WaypointGroup, $WaypointGroup$Type} from "packages/journeymap/client/api/display/$WaypointGroup"
import {$WaypointBase, $WaypointBase$Type} from "packages/journeymap/client/api/model/$WaypointBase"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $Waypoint extends $WaypointBase<($Waypoint)> {
static readonly "VERSION": double

constructor(arg0: string, arg1: string, arg2: string, arg3: $BlockPos$Type)
constructor(arg0: string, arg1: string, arg2: string, arg3: string, arg4: $BlockPos$Type)
constructor(arg0: string, arg1: string, arg2: string, arg3: $ResourceKey$Type<($Level$Type)>, arg4: $BlockPos$Type)
constructor(arg0: string, arg1: string, arg2: $ResourceKey$Type<($Level$Type)>, arg3: $BlockPos$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEnabled"(): boolean
public "getPosition"(arg0: string): $BlockPos
public "getPosition"(): $BlockPos
public "getDisplayOrder"(): integer
public "getCenteredVec"(arg0: string): $Vector3d
public "isTeleportReady"(arg0: string): boolean
public "setGroup"(arg0: $WaypointGroup$Type): $Waypoint
public "getDisplayDimensions"(): (string)[]
public "getDimension"(): string
public "setEnabled"(arg0: boolean): void
public "setPersistent"(arg0: boolean): $Waypoint
public "getVec"(arg0: string): $Vector3d
public "isEditable"(): boolean
public "setEditable"(arg0: boolean): $Waypoint
public "setPosition"(arg0: string, arg1: $BlockPos$Type): $Waypoint
public "isPersistent"(): boolean
public "getGroup"(): $WaypointGroup
get "enabled"(): boolean
get "position"(): $BlockPos
get "displayOrder"(): integer
set "group"(value: $WaypointGroup$Type)
get "displayDimensions"(): (string)[]
get "dimension"(): string
set "enabled"(value: boolean)
set "persistent"(value: boolean)
get "editable"(): boolean
set "editable"(value: boolean)
get "persistent"(): boolean
get "group"(): $WaypointGroup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Waypoint$Type = ($Waypoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Waypoint_ = $Waypoint$Type;
}}
declare module "packages/journeymap/client/api/event/$ClientEvent" {
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $ClientEvent {
readonly "type": $ClientEvent$Type
readonly "dimension": $ResourceKey<($Level)>
readonly "timestamp": long

constructor(arg0: $ClientEvent$Type$Type, arg1: $ResourceKey$Type<($Level$Type)>)
constructor(arg0: $ClientEvent$Type$Type)

public "cancel"(): void
public "isCancelled"(): boolean
public "isCancellable"(): boolean
get "cancelled"(): boolean
get "cancellable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEvent$Type = ($ClientEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEvent_ = $ClientEvent$Type;
}}
declare module "packages/journeymap/client/model/$BlockDataArrays$DataArray" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BlockDataArrays$DataArray<T> {


public "get"(arg0: string, arg1: integer, arg2: integer): T
public "get"(arg0: string): ((T)[])[]
public "clear"(arg0: string): void
public "set"(arg0: string, arg1: integer, arg2: integer, arg3: T): boolean
public "copy"(arg0: string): ((T)[])[]
public "has"(arg0: string): boolean
public "copyTo"(arg0: string, arg1: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDataArrays$DataArray$Type<T> = ($BlockDataArrays$DataArray<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDataArrays$DataArray_<T> = $BlockDataArrays$DataArray$Type<(T)>;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$LabelSpec" {
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Theme$LabelSpec implements $Cloneable {
 "margin": integer
 "background": $Theme$ColorSpec
 "foreground": $Theme$ColorSpec
 "highlight": $Theme$ColorSpec
 "shadow": boolean

constructor()

public "clone"(): $Theme$LabelSpec
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$LabelSpec$Type = ($Theme$LabelSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$LabelSpec_ = $Theme$LabelSpec$Type;
}}
declare module "packages/journeymap/client/api/display/$IThemeButton" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"

export interface $IThemeButton {

 "setEnabled"(arg0: boolean): void
 "m_142518_"(): boolean
 "setTooltip"(...arg0: (string)[]): void
 "getButton"(): $Button
 "setLabels"(arg0: string, arg1: string): void
 "setDrawButton"(arg0: boolean): void
 "setStaysOn"(arg0: boolean): void
 "setToggled"(arg0: boolean): void
 "getToggled"(): boolean
 "toggle"(): void
}

export namespace $IThemeButton {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IThemeButton$Type = ($IThemeButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IThemeButton_ = $IThemeButton$Type;
}}
declare module "packages/journeymap/client/api/option/$CustomOption" {
import {$Option, $Option$Type} from "packages/journeymap/client/api/option/$Option"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $CustomOption<T> extends $Option<(T)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: T)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomOption$Type<T> = ($CustomOption<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomOption_<T> = $CustomOption$Type<(T)>;
}}
declare module "packages/journeymap/client/api/event/$RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar {

 "register"(arg0: string, arg1: string, arg2: long, arg3: $Supplier$Type<(string)>): void

(arg0: string, arg1: string, arg2: long, arg3: $Supplier$Type<(string)>): void
}

export namespace $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar$Type = ($RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar_ = $RegistryEvent$InfoSlotRegistryEvent$InfoSlotRegistrar$Type;
}}
declare module "packages/journeymap/client/ui/option/$OptionScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $OptionScreen extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: string, arg1: $Screen$Type)
constructor(arg0: string)

public "m_7856_"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionScreen$Type = ($OptionScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionScreen_ = $OptionScreen$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawEntityStep" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$EntityDisplay, $EntityDisplay$Type} from "packages/journeymap/client/ui/minimap/$EntityDisplay"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawEntityStep implements $DrawStep {


public "update"(arg0: $EntityDisplay$Type, arg1: $Texture$Type, arg2: $Texture$Type, arg3: integer, arg4: boolean, arg5: boolean, arg6: float): void
public "getPosition"(arg0: double, arg1: double, arg2: $GridRenderer$Type, arg3: boolean): $Point2D$Double
public "getDisplayOrder"(): integer
public "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
public "getModId"(): string
get "displayOrder"(): integer
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawEntityStep$Type = ($DrawEntityStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawEntityStep_ = $DrawEntityStep$Type;
}}
declare module "packages/journeymap/client/ui/$UIManager" {
import {$MiniMap, $MiniMap$Type} from "packages/journeymap/client/ui/minimap/$MiniMap"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$LinkageError, $LinkageError$Type} from "packages/java/lang/$LinkageError"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ServerOptionsManager, $ServerOptionsManager$Type} from "packages/journeymap/client/ui/dialog/$ServerOptionsManager"
import {$MiniMapProperties, $MiniMapProperties$Type} from "packages/journeymap/client/properties/$MiniMapProperties"
import {$MultiplayerOptionsManager, $MultiplayerOptionsManager$Type} from "packages/journeymap/client/ui/dialog/$MultiplayerOptionsManager"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $UIManager extends $Enum<($UIManager)> {
static readonly "INSTANCE": $UIManager


public static "values"(): ($UIManager)[]
public static "valueOf"(arg0: string): $UIManager
public "open"<T extends $JmUI>(arg0: $Class$Type<(T)>, arg1: $JmUI$Type): T
public "open"<T extends $Screen>(arg0: T): T
public "open"<T extends $JmUI>(arg0: $Class$Type<(T)>): T
public "reset"(): void
public "closeAll"(): void
public "drawMiniMap"(arg0: $GuiGraphics$Type): void
public "isMiniMapEnabled"(): boolean
public static "handleLinkageError"(arg0: $LinkageError$Type): void
public "closeCurrent"(): void
public "setMiniMapEnabled"(arg0: boolean): void
public "openServerEditor"(arg0: $JmUI$Type): void
public "openWaypointManager"(arg0: $Waypoint$Type, arg1: $JmUI$Type): void
public "switchMiniMapPreset"(arg0: integer): void
public "switchMiniMapPreset"(): void
public "getMiniMap"(): $MiniMap
public "toggleMinimap"(): void
public "openFullscreenMap"(arg0: $Waypoint$Type): void
public "openFullscreenMap"(): $Fullscreen
public "openOptionsManager"(): void
public "openOptionsManager"(arg0: $JmUI$Type, ...arg1: ($Category$Type)[]): void
public "openWaypointEditor"(arg0: $Waypoint$Type, arg1: boolean, arg2: $JmUI$Type): void
public "openWaypointEditor"(arg0: $Waypoint$Type, arg1: boolean, arg2: $JmUI$Type, arg3: boolean): void
public "getServerEditor"(): $ServerOptionsManager
public "openSplash"(arg0: $JmUI$Type): void
public "openMinimapPosition"(arg0: $JmUI$Type, arg1: $MiniMapProperties$Type): void
public "openAddonOptionsEditor"(arg0: $JmUI$Type): void
public "openMultiplayerEditor"(arg0: $JmUI$Type): void
public "openGridEditor"(arg0: $JmUI$Type): void
public "openInventory"(): void
public "getMultiplayerOptions"(): $MultiplayerOptionsManager
get "miniMapEnabled"(): boolean
set "miniMapEnabled"(value: boolean)
get "miniMap"(): $MiniMap
get "serverEditor"(): $ServerOptionsManager
get "multiplayerOptions"(): $MultiplayerOptionsManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UIManager$Type = (("instance")) | ($UIManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UIManager_ = $UIManager$Type;
}}
declare module "packages/journeymap/client/model/$RegionImageSet$Key" {
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"

export class $RegionImageSet$Key {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "from"(arg0: $RegionCoord$Type): $RegionImageSet$Key
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionImageSet$Key$Type = ($RegionImageSet$Key);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionImageSet$Key_ = $RegionImageSet$Key$Type;
}}
declare module "packages/journeymap/client/texture/$RegionTexture" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$HashSet, $HashSet$Type} from "packages/java/util/$HashSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$DynamicTextureImpl, $DynamicTextureImpl$Type} from "packages/journeymap/client/texture/$DynamicTextureImpl"
import {$RegionTexture$Listener, $RegionTexture$Listener$Type} from "packages/journeymap/client/texture/$RegionTexture$Listener"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $RegionTexture extends $DynamicTextureImpl implements $Texture {
static readonly "NOT_ASSIGNED": integer

constructor(arg0: $NativeImage$Type, arg1: string)

public "getLastImageUpdate"(): long
public "bindNeeded"(): boolean
public "getDirtyAreas"(): $Set<($ChunkPos)>
public "isDefunct"(): boolean
public "remove"(): void
public "toString"(): string
public "close"(): void
public "load"(arg0: $ResourceManager$Type): void
public "isBound"(): boolean
public "getWidth"(): integer
public "getHeight"(): integer
public "bind"(): void
public "setDisplayHeight"(arg0: integer): void
public "getNativeImage"(): $NativeImage
public "getScaledImage"(arg0: float): $Texture
public "setNativeImage"(arg0: $NativeImage$Type): void
public "setNativeImage"(arg0: $NativeImage$Type, arg1: boolean): void
public "setNativeImage"(arg0: $NativeImage$Type, arg1: boolean, arg2: $HashSet$Type<($ChunkPos$Type)>): void
public "setDisplayWidth"(arg0: integer): void
public "getTextureId"(): integer
public "getAlpha"(): float
public "setAlpha"(arg0: float): void
public "getSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $NativeImage
public "addListener"(arg0: $RegionTexture$Listener$Type<(any)>): void
public "hasImage"(): boolean
public "getLocation"(): $ResourceLocation
public "release"(): void
public "getRGB"(arg0: integer, arg1: integer): integer
get "lastImageUpdate"(): long
get "dirtyAreas"(): $Set<($ChunkPos)>
get "defunct"(): boolean
get "bound"(): boolean
get "width"(): integer
get "height"(): integer
set "displayHeight"(value: integer)
get "nativeImage"(): $NativeImage
set "nativeImage"(value: $NativeImage$Type)
set "displayWidth"(value: integer)
get "textureId"(): integer
get "alpha"(): float
set "alpha"(value: float)
get "location"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionTexture$Type = ($RegionTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionTexture_ = $RegionTexture$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/layer/$ModOverlayLayer" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$Layer, $Layer$Type} from "packages/journeymap/client/ui/fullscreen/layer/$Layer"

export class $ModOverlayLayer extends $Layer {

constructor(arg0: $Fullscreen$Type)

public "propagateClick"(): boolean
public "onMouseClick"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: integer, arg5: boolean, arg6: float): $List<($DrawStep)>
public "onMouseMove"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: float, arg5: boolean): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModOverlayLayer$Type = ($ModOverlayLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModOverlayLayer_ = $ModOverlayLayer$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$Effect" {
import {$Selectable, $Selectable$Type} from "packages/journeymap/client/ui/minimap/$Selectable"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $Effect implements $Selectable {

constructor()

public static "getInstance"(): $Effect
public "tick"(): void
public "canPotionShift"(): boolean
public "withinBounds"(arg0: double, arg1: double): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "getPotionEffectsLocation"(): $Vec2
public "renderBorder"(arg0: $GuiGraphics$Type, arg1: integer): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public static "effectProcessor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $MobEffect$Type): (integer)[]
public "withinScreenBounds"(arg0: double, arg1: double): $Vec2
get "instance"(): $Effect
get "potionEffectsLocation"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Effect$Type = ($Effect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Effect_ = $Effect$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeEventHandlerManager" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ForgeEventHandlerManager {

constructor()

public static "register"(arg0: $ForgeEventHandlerManager$EventHandler$Type): void
public static "unregister"(arg0: $Class$Type<(any)>): void
public static "getHandlers"(): $HashMap<($Class<(any)>), ($ForgeEventHandlerManager$EventHandler)>
public static "unregisterAll"(): void
public static "registerHandlers"(): void
get "handlers"(): $HashMap<($Class<(any)>), ($ForgeEventHandlerManager$EventHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventHandlerManager$Type = ($ForgeEventHandlerManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventHandlerManager_ = $ForgeEventHandlerManager$Type;
}}
declare module "packages/journeymap/client/feature/$Policy" {
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Feature, $Feature$Type} from "packages/journeymap/client/feature/$Feature"

export class $Policy {

constructor(arg0: $Feature$Type, arg1: boolean, arg2: boolean)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "isCurrentlyAllowed"(): boolean
public static "bulkCreate"(arg0: boolean, arg1: boolean): $Set<($Policy)>
public static "bulkCreate"(arg0: $EnumSet$Type<($Feature$Type)>, arg1: boolean, arg2: boolean): $Set<($Policy)>
get "currentlyAllowed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Policy$Type = ($Policy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Policy_ = $Policy$Type;
}}
declare module "packages/journeymap/client/render/map/$TilePos" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

export class $TilePos implements $Comparable<($TilePos)> {
readonly "deltaX": integer
readonly "deltaZ": integer


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $TilePos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TilePos$Type = ($TilePos);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TilePos_ = $TilePos$Type;
}}
declare module "packages/journeymap/client/properties/$ClientCategory" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $ClientCategory {
static readonly "values": $List<($Category)>
static readonly "MiniMap1": $Category
static readonly "MiniMap2": $Category
static readonly "FullMap": $Category
static readonly "WebMap": $Category
static readonly "Waypoint": $Category
static readonly "WaypointBeacon": $Category
static readonly "Cartography": $Category
static readonly "Advanced": $Category
static readonly "MinimapPosition": $Category

constructor()

public static "valueOf"(arg0: string): $Category
public static "create"(arg0: string, arg1: string, arg2: string): $Category
public static "create"(arg0: string, arg1: string, arg2: boolean): $Category
public static "create"(arg0: string, arg1: string): $Category
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCategory$Type = ($ClientCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCategory_ = $ClientCategory$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$PopupMenuEvent$FullscreenPopupMenuEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$PopupMenuEvent, $PopupMenuEvent$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $PopupMenuEvent$FullscreenPopupMenuEvent extends $PopupMenuEvent {

constructor(arg0: $ModPopupMenu$Type, arg1: $IFullscreen$Type)
constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuEvent$FullscreenPopupMenuEvent$Type = ($PopupMenuEvent$FullscreenPopupMenuEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuEvent$FullscreenPopupMenuEvent_ = $PopupMenuEvent$FullscreenPopupMenuEvent$Type;
}}
declare module "packages/journeymap/client/feature/$FeatureManager" {
import {$GlobalProperties, $GlobalProperties$Type} from "packages/journeymap/common/properties/$GlobalProperties"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Feature, $Feature$Type} from "packages/journeymap/client/feature/$Feature"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FeatureManager {


public static "getInstance"(): $FeatureManager
public "reset"(): void
public "isAllowed"(arg0: $Feature$Type): boolean
public "getAllowedFeatures"(): $Map<($Feature), (boolean)>
public "updateDimensionFeatures"(arg0: $GlobalProperties$Type): void
public "getPolicyDetails"(): string
public "disableFeature"(arg0: $Context$MapType$Type, arg1: $Context$UI$Type, arg2: $ResourceKey$Type<($Level$Type)>): void
get "instance"(): $FeatureManager
get "allowedFeatures"(): $Map<($Feature), (boolean)>
get "policyDetails"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FeatureManager$Type = ($FeatureManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FeatureManager_ = $FeatureManager$Type;
}}
declare module "packages/journeymap/client/api/impl/$DisplayUpdateEventThrottle" {
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$DisplayUpdateEvent, $DisplayUpdateEvent$Type} from "packages/journeymap/client/api/event/$DisplayUpdateEvent"

export class $DisplayUpdateEventThrottle {


public "add"(arg0: $DisplayUpdateEvent$Type): void
public "iterator"(): $Iterator<($DisplayUpdateEvent)>
public "isReady"(): boolean
get "ready"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayUpdateEventThrottle$Type = ($DisplayUpdateEventThrottle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayUpdateEventThrottle_ = $DisplayUpdateEventThrottle$Type;
}}
declare module "packages/journeymap/client/event/handlers/$StateTickHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StateTickHandler {

constructor()

public "onClientTick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StateTickHandler$Type = ($StateTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StateTickHandler_ = $StateTickHandler$Type;
}}
declare module "packages/journeymap/client/event/handlers/$KeyEventHandler" {
import {$KeyEvent, $KeyEvent$Type} from "packages/journeymap/client/event/handlers/keymapping/$KeyEvent"
import {$KeyBindingAction, $KeyBindingAction$Type} from "packages/journeymap/client/event/handlers/keymapping/$KeyBindingAction"
import {$UpdateAwareKeyBinding, $UpdateAwareKeyBinding$Type} from "packages/journeymap/client/event/handlers/keymapping/$UpdateAwareKeyBinding"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ListMultimap, $ListMultimap$Type} from "packages/com/google/common/collect/$ListMultimap"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $KeyEventHandler {
 "kbMapZoomin": $UpdateAwareKeyBinding
 "kbMapZoomout": $UpdateAwareKeyBinding
 "kbMapToggleType": $UpdateAwareKeyBinding
 "kbCreateWaypoint": $UpdateAwareKeyBinding
 "kbToggleAllWaypoints": $UpdateAwareKeyBinding
 "kbFullscreenCreateWaypoint": $UpdateAwareKeyBinding
 "kbFullscreenChatPosition": $UpdateAwareKeyBinding
 "kbFullscreenToggle": $UpdateAwareKeyBinding
 "kbWaypointManager": $UpdateAwareKeyBinding
 "kbMinimapToggle": $UpdateAwareKeyBinding
 "kbMinimapPreset": $UpdateAwareKeyBinding
 "kbFullmapOptionsManager": $UpdateAwareKeyBinding
 "kbFullmapPanNorth": $UpdateAwareKeyBinding
 "kbFullmapPanSouth": $UpdateAwareKeyBinding
 "kbFullmapPanEast": $UpdateAwareKeyBinding
 "kbFullmapPanWest": $UpdateAwareKeyBinding
 "kbFullmapButtonHide": $UpdateAwareKeyBinding
 "sortActionsNeeded": boolean

constructor(arg0: $KeyEvent$Type)

public "getInGuiKeybindings"(): $List<($UpdateAwareKeyBinding)>
public "registerActions"(): $KeyEventHandler
public "getPressedKey"(arg0: $ListMultimap$Type<(integer), ($KeyBindingAction$Type)>): integer
public "onMouseEvent"(arg0: integer, arg1: $Screen$Type): boolean
public "onGuiKeyboardEvent"(arg0: $Screen$Type, arg1: integer): boolean
public "onGameKeyboardEvent"(arg0: integer): boolean
get "inGuiKeybindings"(): $List<($UpdateAwareKeyBinding)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyEventHandler$Type = ($KeyEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyEventHandler_ = $KeyEventHandler$Type;
}}
declare module "packages/journeymap/client/render/map/$TileDrawStep" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$RegionTexture$Listener, $RegionTexture$Listener$Type} from "packages/journeymap/client/texture/$RegionTexture$Listener"
import {$RegionTexture, $RegionTexture$Type} from "packages/journeymap/client/texture/$RegionTexture"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $TileDrawStep implements $RegionTexture$Listener<($RegionTexture)> {

constructor(arg0: $RegionCoord$Type, arg1: $MapType$Type, arg2: integer, arg3: boolean, arg4: integer, arg5: integer, arg6: integer, arg7: integer)

public "clearTexture"(): void
public "toString"(): string
public "hashCode"(): integer
public "cacheKey"(): string
public static "toCacheKey"(arg0: $RegionCoord$Type, arg1: $MapType$Type, arg2: integer, arg3: boolean, arg4: integer, arg5: integer, arg6: integer, arg7: integer): string
public "getMapType"(): $MapType
public "getZoom"(): integer
public "textureImageUpdated"(arg0: $RegionTexture$Type): void
public "getScaledRegionArea"(): $NativeImage
get "mapType"(): $MapType
get "zoom"(): integer
get "scaledRegionArea"(): $NativeImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TileDrawStep$Type = ($TileDrawStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TileDrawStep_ = $TileDrawStep$Type;
}}
declare module "packages/journeymap/client/task/multi/$RenderSpec$RevealShape" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $RenderSpec$RevealShape extends $Enum<($RenderSpec$RevealShape)> implements $KeyedEnum {
static readonly "Square": $RenderSpec$RevealShape
static readonly "Circle": $RenderSpec$RevealShape
readonly "key": string


public "toString"(): string
public static "values"(): ($RenderSpec$RevealShape)[]
public static "valueOf"(arg0: string): $RenderSpec$RevealShape
public "getKey"(): string
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSpec$RevealShape$Type = (("square") | ("circle")) | ($RenderSpec$RevealShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSpec$RevealShape_ = $RenderSpec$RevealShape$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSquare" {
import {$Theme$ImageSpec, $Theme$ImageSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ImageSpec"
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"
import {$Theme$LabelSpec, $Theme$LabelSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$LabelSpec"
import {$Theme$Minimap$MinimapSpec, $Theme$Minimap$MinimapSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSpec"

export class $Theme$Minimap$MinimapSquare extends $Theme$Minimap$MinimapSpec {
 "topLeft": $Theme$ImageSpec
 "top": $Theme$ImageSpec
 "topRight": $Theme$ImageSpec
 "right": $Theme$ImageSpec
 "bottomRight": $Theme$ImageSpec
 "bottom": $Theme$ImageSpec
 "bottomLeft": $Theme$ImageSpec
 "left": $Theme$ImageSpec
 "margin": integer
 "labelTop": $Theme$LabelSpec
 "labelTopInside": boolean
 "labelBottom": $Theme$LabelSpec
 "labelBottomInside": boolean
 "compassLabel": $Theme$LabelSpec
 "compassPoint": $Theme$ImageSpec
 "compassPointLabelPad": integer
 "compassPointOffset": double
 "compassShowNorth": boolean
 "compassShowSouth": boolean
 "compassShowEast": boolean
 "compassShowWest": boolean
 "waypointOffset": double
 "reticle": $Theme$ColorSpec
 "reticleHeading": $Theme$ColorSpec
 "reticleThickness": double
 "reticleHeadingThickness": double
 "reticleOffsetOuter": integer
 "reticleOffsetInner": integer
 "frame": $Theme$ColorSpec
 "prefix": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Minimap$MinimapSquare$Type = ($Theme$Minimap$MinimapSquare);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Minimap$MinimapSquare_ = $Theme$Minimap$MinimapSquare$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/layer/$KeybindingInfoLayer" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$Layer, $Layer$Type} from "packages/journeymap/client/ui/fullscreen/layer/$Layer"

export class $KeybindingInfoLayer extends $Layer {

constructor(arg0: $Fullscreen$Type)

public "propagateClick"(): boolean
public "onMouseClick"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: integer, arg5: boolean, arg6: float): $List<($DrawStep)>
public "onMouseMove"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: float, arg5: boolean): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeybindingInfoLayer$Type = ($KeybindingInfoLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeybindingInfoLayer_ = $KeybindingInfoLayer$Type;
}}
declare module "packages/journeymap/client/task/multi/$BaseMapTask" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$ChunkRenderController, $ChunkRenderController$Type} from "packages/journeymap/client/cartography/$ChunkRenderController"

export class $BaseMapTask implements $ITask {

constructor(arg0: $ChunkRenderController$Type, arg1: $Level$Type, arg2: $MapType$Type, arg3: $Collection$Type<($ChunkPos$Type)>, arg4: boolean, arg5: boolean, arg6: integer)

public "toString"(): string
public "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "initTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "getMaxRuntime"(): integer
get "maxRuntime"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseMapTask$Type = ($BaseMapTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseMapTask_ = $BaseMapTask$Type;
}}
declare module "packages/journeymap/client/api/display/$Context$UI" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Context, $Context$Type} from "packages/journeymap/client/api/display/$Context"

export class $Context$UI extends $Enum<($Context$UI)> implements $Context {
static readonly "Any": $Context$UI
static readonly "Fullscreen": $Context$UI
static readonly "Minimap": $Context$UI
static readonly "Webmap": $Context$UI


public static "values"(): ($Context$UI)[]
public static "valueOf"(arg0: string): $Context$UI
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Context$UI$Type = (("fullscreen") | ("minimap") | ("webmap") | ("any")) | ($Context$UI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Context$UI_ = $Context$UI$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent$EntityType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EntityRadarUpdateEvent$EntityType extends $Enum<($EntityRadarUpdateEvent$EntityType)> {
static readonly "MOB": $EntityRadarUpdateEvent$EntityType
static readonly "PLAYER": $EntityRadarUpdateEvent$EntityType


public static "values"(): ($EntityRadarUpdateEvent$EntityType)[]
public static "valueOf"(arg0: string): $EntityRadarUpdateEvent$EntityType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRadarUpdateEvent$EntityType$Type = (("mob") | ("player")) | ($EntityRadarUpdateEvent$EntityType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRadarUpdateEvent$EntityType_ = $EntityRadarUpdateEvent$EntityType$Type;
}}
declare module "packages/journeymap/client/waypoint/$WaypointGroup" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$LinkedTreeMap, $LinkedTreeMap$Type} from "packages/com/google/gson/internal/$LinkedTreeMap"

export class $WaypointGroup implements $Comparable<($WaypointGroup)> {
static readonly "DEFAULT": $WaypointGroup
static readonly "VERSION": double
static readonly "GSON": $Gson

constructor(arg0: string, arg1: string)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $WaypointGroup$Type): integer
public static "from"(arg0: $LinkedTreeMap$Type<(any), (any)>): $WaypointGroup
public "getKey"(): string
public "setName"(arg0: string): $WaypointGroup
public "isDirty"(): boolean
public "setColor"(arg0: integer): $WaypointGroup
public "setColor"(arg0: string): $WaypointGroup
public "setDirty"(arg0: boolean): $WaypointGroup
public "setDirty"(): $WaypointGroup
public "isEnable"(): boolean
public "setEnable"(arg0: boolean): $WaypointGroup
public "setOrigin"(arg0: string): $WaypointGroup
public "getIcon"(): string
public "getColor"(): integer
public "setIcon"(arg0: string): $WaypointGroup
public static "getNamedGroup"(arg0: string, arg1: string): $WaypointGroup
public "getOrigin"(): string
get "name"(): string
get "key"(): string
set "name"(value: string)
get "dirty"(): boolean
set "color"(value: integer)
set "color"(value: string)
set "dirty"(value: boolean)
get "enable"(): boolean
set "enable"(value: boolean)
set "origin"(value: string)
get "icon"(): string
get "color"(): integer
set "icon"(value: string)
get "origin"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointGroup$Type = ($WaypointGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointGroup_ = $WaypointGroup$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$MultiplayerOptionsManager" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ServerOptionsManager, $ServerOptionsManager$Type} from "packages/journeymap/client/ui/dialog/$ServerOptionsManager"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MultiplayerOptionsManager extends $ServerOptionsManager {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $JmUI$Type)

public "m_7856_"(): void
public "setData"(arg0: string): void
set "data"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiplayerOptionsManager$Type = ($MultiplayerOptionsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiplayerOptionsManager_ = $MultiplayerOptionsManager$Type;
}}
declare module "packages/journeymap/client/api/$IClientPlugin" {
import {$IClientAPI, $IClientAPI$Type} from "packages/journeymap/client/api/$IClientAPI"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"

export interface $IClientPlugin {

 "initialize"(arg0: $IClientAPI$Type): void
 "onEvent"(arg0: $ClientEvent$Type): void
 "getModId"(): string
}

export namespace $IClientPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientPlugin$Type = ($IClientPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientPlugin_ = $IClientPlugin$Type;
}}
declare module "packages/journeymap/client/cartography/render/$NetherRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$CaveRenderer, $CaveRenderer$Type} from "packages/journeymap/client/cartography/render/$CaveRenderer"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $NetherRenderer extends $CaveRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor()

public "getAmbientColor"(): (float)[]
public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetherRenderer$Type = ($NetherRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetherRenderer_ = $NetherRenderer$Type;
}}
declare module "packages/journeymap/client/data/$WaypointsData" {
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointsData extends $CacheLoader<($Class), ($Collection<($Waypoint)>)> {

constructor()

public "load"(arg0: $Class$Type<(any)>): $Collection<($Waypoint)>
public "getTTL"(): long
public static "isManagerEnabled"(): boolean
get "tTL"(): long
get "managerEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointsData$Type = ($WaypointsData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointsData_ = $WaypointsData$Type;
}}
declare module "packages/journeymap/client/waypoint/$WaypointStore" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$WaypointGroup, $WaypointGroup$Type} from "packages/journeymap/client/waypoint/$WaypointGroup"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointStore extends $Enum<($WaypointStore)> {
static readonly "INSTANCE": $WaypointStore


public "add"(arg0: $Waypoint$Type): void
public "remove"(arg0: $Waypoint$Type, arg1: boolean): void
public "get"(arg0: string): $Waypoint
public static "values"(): ($WaypointStore)[]
public "load"(arg0: $Collection$Type<($Waypoint$Type)>, arg1: boolean): void
public static "valueOf"(arg0: string): $WaypointStore
public "save"(arg0: $Waypoint$Type, arg1: boolean): void
public "reset"(): void
public "getAll"(): $Collection<($Waypoint)>
public "getAll"(arg0: $WaypointGroup$Type): $Collection<($Waypoint)>
public "bulkSave"(): void
public "getLoadedDimensions"(): $List<(string)>
public "hasLoaded"(): boolean
get "all"(): $Collection<($Waypoint)>
get "loadedDimensions"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointStore$Type = (("instance")) | ($WaypointStore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointStore_ = $WaypointStore$Type;
}}
declare module "packages/journeymap/client/task/multi/$MapPlayerTask" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$BaseMapTask, $BaseMapTask$Type} from "packages/journeymap/client/task/multi/$BaseMapTask"
import {$File, $File$Type} from "packages/java/io/$File"
import {$MapPlayerTask$MapPlayerTaskBatch, $MapPlayerTask$MapPlayerTaskBatch$Type} from "packages/journeymap/client/task/multi/$MapPlayerTask$MapPlayerTaskBatch"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$ChunkRenderController, $ChunkRenderController$Type} from "packages/journeymap/client/cartography/$ChunkRenderController"

export class $MapPlayerTask extends $BaseMapTask {


public static "create"(arg0: $ChunkRenderController$Type, arg1: $EntityDTO$Type): $MapPlayerTask$MapPlayerTaskBatch
public static "getDebugStats"(): (string)[]
public static "forceNearbyRemap"(): void
public static "getSimpleStats"(): string
public "initTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "getMaxRuntime"(): integer
public static "getlastTaskCompleted"(): long
public static "addTempDebugMessage"(arg0: string, arg1: string): void
public static "removeTempDebugMessage"(arg0: string): void
get "debugStats"(): (string)[]
get "simpleStats"(): string
get "maxRuntime"(): integer
get "lastTaskCompleted"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapPlayerTask$Type = ($MapPlayerTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapPlayerTask_ = $MapPlayerTask$Type;
}}
declare module "packages/journeymap/client/api/impl/$ClientEventManager" {
import {$DeathWaypointEvent, $DeathWaypointEvent$Type} from "packages/journeymap/client/api/event/$DeathWaypointEvent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"
import {$PluginWrapper, $PluginWrapper$Type} from "packages/journeymap/client/api/impl/$PluginWrapper"
import {$DisplayUpdateEvent, $DisplayUpdateEvent$Type} from "packages/journeymap/client/api/event/$DisplayUpdateEvent"

export class $ClientEventManager {

constructor(arg0: $Collection$Type<($PluginWrapper$Type)>)

public "fireEvent"(arg0: $ClientEvent$Type): void
public "fireMappingEvent"(arg0: boolean, arg1: $ResourceKey$Type<($Level$Type)>): void
public "fireDisplayUpdateEvent"(arg0: $DisplayUpdateEvent$Type): void
public "fireNextClientEvents"(): void
public "fireDeathpointEvent"(arg0: $DeathWaypointEvent$Type): void
public "canFireClientEvent"(arg0: $ClientEvent$Type$Type): boolean
public "updateSubscribedTypes"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEventManager$Type = ($ClientEventManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEventManager_ = $ClientEventManager$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$ResourcesKt" {
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $ResourcesKt {


public static "resourcesGet"(arg0: $RouteHandler$Type): any
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
declare module "packages/journeymap/client/task/multi/$TaskController" {
import {$ITaskManager, $ITaskManager$Type} from "packages/journeymap/client/task/multi/$ITaskManager"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $TaskController {

constructor()

public "clear"(): void
public "isActive"(): boolean
public "performTasks"(): void
public "enableTasks"(): void
public "toggleTask"(arg0: $ITaskManager$Type, arg1: boolean, arg2: any): void
public "toggleTask"(arg0: $Class$Type<(any)>, arg1: boolean, arg2: any): void
public "queueOneOff"(arg0: $Runnable$Type): void
public "disableTasks"(): void
public "isTaskManagerEnabled"(arg0: $Class$Type<(any)>): boolean
public "hasRunningTask"(): boolean
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TaskController$Type = ($TaskController);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TaskController_ = $TaskController$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeToolbar" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$ButtonList, $ButtonList$Type} from "packages/journeymap/client/ui/component/$ButtonList"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$ThemeButton, $ThemeButton$Type} from "packages/journeymap/client/ui/theme/$ThemeButton"
import {$Button as $Button$0, $Button$Type as $Button$0$Type} from "packages/journeymap/client/ui/component/$Button"
import {$Theme$Container$Toolbar$ToolbarSpec, $Theme$Container$Toolbar$ToolbarSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Container$Toolbar$ToolbarSpec"
import {$ButtonList$Direction, $ButtonList$Direction$Type} from "packages/journeymap/client/ui/component/$ButtonList$Direction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ButtonList$Layout, $ButtonList$Layout$Type} from "packages/journeymap/client/ui/component/$ButtonList$Layout"
import {$IThemeToolbarInternal, $IThemeToolbarInternal$Type} from "packages/journeymap/client/ui/theme/$IThemeToolbarInternal"

export class $ThemeToolbar extends $Button$0 implements $IThemeToolbarInternal {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Theme$Type, arg1: $ButtonList$Type)
constructor(arg0: $Theme$Type, ...arg1: ($ThemeButton$Type)[])
constructor(arg0: $Theme$Type, ...arg1: ($Button$0$Type)[])

public "add"<B extends $Button$0>(...arg0: (B)[]): void
public "contains"(arg0: $Button$Type): boolean
public "reverse"(): $ButtonList
public "equalizeWidths"(arg0: $Font$Type): void
public "equalizeWidths"(arg0: $Font$Type, arg1: integer, arg2: integer): void
public "getBottomY"(): integer
public "updateTextures"(): $Theme$Container$Toolbar$ToolbarSpec
public "layoutVertical"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "getFormattedTooltip"(): $List<($FormattedCharSequence)>
public "setLayout"(arg0: $ButtonList$Layout$Type, arg1: $ButtonList$Direction$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "addAllButtons"(arg0: $JmUI$Type): void
public "getMiddleY"(): integer
public "updateTheme"(arg0: $Theme$Type): void
public "layoutFilledHorizontal"(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean): $ButtonList
public "getToolbarSpec"(): $Theme$Container$Toolbar$ToolbarSpec
public "getVMargin"(): integer
public "getCenterX"(): integer
public "getHMargin"(): integer
public "getRightX"(): integer
public "layoutHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "layoutHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer, arg4: boolean): $ButtonList
public "setDrawToolbar"(arg0: boolean): void
public "layoutCenteredHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "layoutCenteredVertical"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "setPosition"(arg0: integer, arg1: integer): void
public "updateLayout"(): void
public "getToolbarHeight"(): integer
public "getToolbarX"(): integer
public "getToolbarY"(): integer
public "setLayoutVertical"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
public "setToolbarPosition"(arg0: integer, arg1: integer): void
public "setReverse"(): void
public "getToolbarWidth"(): integer
public "setLayoutDistributedHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
public "setLayoutCenteredHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
public "setLayoutHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
public "layoutDistributedHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): $ButtonList
public "setLayoutCenteredVertical"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
/**
 * 
 * @deprecated
 */
public "getHeight"(): integer
/**
 * 
 * @deprecated
 */
public "getWidth"(): integer
/**
 * 
 * @deprecated
 */
public "getX"(): integer
/**
 * 
 * @deprecated
 */
public "getY"(): integer
public "getY"(): integer
public "getX"(): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "setPosition"(arg0: integer, arg1: integer): void
get "bottomY"(): integer
get "formattedTooltip"(): $List<($FormattedCharSequence)>
get "middleY"(): integer
get "toolbarSpec"(): $Theme$Container$Toolbar$ToolbarSpec
get "vMargin"(): integer
get "centerX"(): integer
get "hMargin"(): integer
get "rightX"(): integer
set "drawToolbar"(value: boolean)
get "toolbarHeight"(): integer
get "toolbarX"(): integer
get "toolbarY"(): integer
get "toolbarWidth"(): integer
get "height"(): integer
get "width"(): integer
get "x"(): integer
get "y"(): integer
get "y"(): integer
get "x"(): integer
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeToolbar$Type = ($ThemeToolbar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeToolbar_ = $ThemeToolbar$Type;
}}
declare module "packages/journeymap/client/ui/component/$PropertyDropdownButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$IConfigFieldHolder, $IConfigFieldHolder$Type} from "packages/journeymap/client/ui/component/$IConfigFieldHolder"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DropDownItem, $DropDownItem$Type} from "packages/journeymap/client/ui/component/$DropDownItem"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DropDownButton, $DropDownButton$Type} from "packages/journeymap/client/ui/component/$DropDownButton"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $PropertyDropdownButton<T> extends $DropDownButton implements $IConfigFieldHolder<($ConfigField<(T)>)> {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Collection$Type<(T)>, arg1: string, arg2: $ConfigField$Type<(T)>, arg3: $Button$OnPress$Type)
constructor(arg0: $Collection$Type<(T)>, arg1: string, arg2: $ConfigField$Type<(T)>)

public "getField"(): $ConfigField<(T)>
public "setValue"(arg0: T): void
public "refresh"(): void
public "getConfigField"(): $ConfigField<(T)>
public "getFitWidth"(arg0: $Font$Type): integer
public "setSelected"(arg0: $DropDownItem$Type): void
public "setWidth"(arg0: integer): void
public "getWidth"(): integer
get "field"(): $ConfigField<(T)>
set "value"(value: T)
get "configField"(): $ConfigField<(T)>
set "selected"(value: $DropDownItem$Type)
set "width"(value: integer)
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyDropdownButton$Type<T> = ($PropertyDropdownButton<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyDropdownButton_<T> = $PropertyDropdownButton$Type<(T)>;
}}
declare module "packages/journeymap/client/api/option/$IntegerOption" {
import {$Option, $Option$Type} from "packages/journeymap/client/api/option/$Option"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $IntegerOption extends $Option<(integer)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: integer, arg4: integer, arg5: integer)

public "getMinValue"(): integer
public "getMaxValue"(): integer
get "minValue"(): integer
get "maxValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerOption$Type = ($IntegerOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerOption_ = $IntegerOption$Type;
}}
declare module "packages/journeymap/client/data/$ImagesData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ImagesData {
static readonly "PARAM_SINCE": string

constructor(arg0: long)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImagesData$Type = ($ImagesData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImagesData_ = $ImagesData$Type;
}}
declare module "packages/journeymap/client/api/$ClientPlugin" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ClientPlugin extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ClientPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlugin$Type = ($ClientPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlugin_ = $ClientPlugin$Type;
}}
declare module "packages/journeymap/client/model/$RegionCoord" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $RegionCoord implements $Comparable<($RegionCoord)> {
readonly "worldDir": $File
readonly "dimDir": $Path
readonly "regionX": integer
readonly "regionZ": integer
readonly "dimension": $ResourceKey<($Level)>

constructor(arg0: $File$Type, arg1: integer, arg2: integer, arg3: $ResourceKey$Type<($Level$Type)>)

public static "getRegionPos"(arg0: integer): integer
public static "getMaxChunkZ"(arg0: integer): integer
public "getMaxChunkZ"(): integer
public static "getMaxChunkX"(arg0: integer): integer
public "getMaxChunkX"(): integer
public "getMinChunkX"(): integer
public static "getMinChunkX"(arg0: integer): integer
public "getMinChunkZ"(): integer
public static "getMinChunkZ"(arg0: integer): integer
public "getZOffset"(arg0: integer): integer
public "getMaxChunkCoord"(): $ChunkPos
public "getMinChunkCoord"(): $ChunkPos
public static "fromRegionPos"(arg0: $File$Type, arg1: integer, arg2: integer, arg3: $ResourceKey$Type<($Level$Type)>): $RegionCoord
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $RegionCoord$Type): integer
public "exists"(): boolean
public "cacheKey"(): string
public static "toCacheKey"(arg0: $Path$Type, arg1: integer, arg2: integer): string
public static "fromChunkPos"(arg0: $File$Type, arg1: $MapType$Type, arg2: integer, arg3: integer): $RegionCoord
public "getChunkCoordsInRegion"(): $List<($ChunkPos)>
public "getXOffset"(arg0: integer): integer
get "maxChunkZ"(): integer
get "maxChunkX"(): integer
get "minChunkX"(): integer
get "minChunkZ"(): integer
get "maxChunkCoord"(): $ChunkPos
get "minChunkCoord"(): $ChunkPos
get "chunkCoordsInRegion"(): $List<($ChunkPos)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionCoord$Type = ($RegionCoord);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionCoord_ = $RegionCoord$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/routes/$DataKt" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $DataKt {


public static "dataGet"(arg0: $RouteHandler$Type): any
public static "getDataTypesRequiringSince"(): $List<(string)>
get "dataTypesRequiringSince"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataKt$Type = ($DataKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataKt_ = $DataKt$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$ColorSpec" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Theme$ColorSpec implements $Cloneable {
 "color": string
 "alpha": float

constructor()
constructor(arg0: string, arg1: float)

public "clone"(): $Theme$ColorSpec
public "getColor"(): integer
get "color"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$ColorSpec$Type = ($Theme$ColorSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$ColorSpec_ = $Theme$ColorSpec$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/$RoutesKt" {
import {$Function1, $Function1$Type} from "packages/info/journeymap/shaded/kotlin/kotlin/jvm/functions/$Function1"
import {$RouteHandler, $RouteHandler$Type} from "packages/info/journeymap/shaded/kotlin/spark/kotlin/$RouteHandler"

export class $RoutesKt {


public static "wrapForError"(arg0: $Function1$Type<(any), (any)>): $Function1<($RouteHandler), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RoutesKt$Type = ($RoutesKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RoutesKt_ = $RoutesKt$Type;
}}
declare module "packages/journeymap/client/ui/option/$CategorySlot" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$Slot, $Slot$Type} from "packages/journeymap/client/ui/component/$Slot"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CategorySlot extends $Slot implements $Comparable<($CategorySlot)> {

constructor(arg0: $Category$Type)

public "add"(arg0: $Slot$Type): $CategorySlot
public "compareTo"(arg0: $CategorySlot$Type): integer
public "clear"(): void
public "size"(): integer
public "contains"(arg0: $SlotMetadata$Type<(any)>): boolean
public "sort"(): void
public "getLastPressed"(): $SlotMetadata<(any)>
public "setEnabled"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getCategory"(): $Category
public "isSelected"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "getColumnWidth"(): integer
public "setSelected"(arg0: boolean): void
public "getCurrentColumnWidth"(): integer
public "getAllChildMetadata"(): $List<($SlotMetadata)>
public "getMetadata"(): $Collection<($SlotMetadata)>
public "getCurrentTooltip"(): $SlotMetadata<(any)>
public "getChildSlots"(arg0: integer, arg1: integer): $List<($Slot)>
public "getCurrentColumns"(): integer
get "lastPressed"(): $SlotMetadata<(any)>
set "enabled"(value: boolean)
get "category"(): $Category
get "selected"(): boolean
get "columnWidth"(): integer
set "selected"(value: boolean)
get "currentColumnWidth"(): integer
get "allChildMetadata"(): $List<($SlotMetadata)>
get "metadata"(): $Collection<($SlotMetadata)>
get "currentTooltip"(): $SlotMetadata<(any)>
get "currentColumns"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CategorySlot$Type = ($CategorySlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CategorySlot_ = $CategorySlot$Type;
}}
declare module "packages/journeymap/client/ui/component/$TextBox" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$EditBox, $EditBox$Type} from "packages/net/minecraft/client/gui/components/$EditBox"

export class $TextBox extends $EditBox {
static readonly "BACKWARDS": integer
static readonly "FORWARDS": integer
static readonly "DEFAULT_TEXT_COLOR": integer
readonly "font": $Font
 "displayPos": integer
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: any, arg1: $Font$Type, arg2: integer, arg3: integer)
constructor(arg0: any, arg1: $Font$Type, arg2: integer, arg3: integer, arg4: boolean, arg5: boolean)

public "isNumeric"(): boolean
public "setText"(arg0: any): void
public "getBottomY"(): integer
public "setClamp"(arg0: integer, arg1: integer): void
public "setMinLength"(arg0: integer): void
public "hasMinLength"(): boolean
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "setFocused"(arg0: boolean): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "clamp"(arg0: string): integer
public "clamp"(): integer
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "insertText"(arg0: string): void
public "setCursorPosition"(arg0: integer): void
public "getMiddleY"(): integer
public "getCenterX"(): integer
public "getRightX"(): integer
public "selectAll"(): void
public "getHeight"(): integer
public "setHeight"(arg0: integer): void
public "isHovered"(): boolean
public "setWidth"(arg0: integer): void
public "setX"(arg0: integer): void
public "setY"(arg0: integer): void
public "getWidth"(): integer
public "getX"(): integer
public "getY"(): integer
public "isAllSelected"(): boolean
get "numeric"(): boolean
set "text"(value: any)
get "bottomY"(): integer
set "minLength"(value: integer)
set "focused"(value: boolean)
set "cursorPosition"(value: integer)
get "middleY"(): integer
get "centerX"(): integer
get "rightX"(): integer
get "height"(): integer
set "height"(value: integer)
get "hovered"(): boolean
set "width"(value: integer)
set "x"(value: integer)
set "y"(value: integer)
get "width"(): integer
get "x"(): integer
get "y"(): integer
get "allSelected"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextBox$Type = ($TextBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextBox_ = $TextBox$Type;
}}
declare module "packages/journeymap/client/ui/$GuiHooks" {
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $GuiHooks {

constructor()

public static "getGuiFarPlane"(): float
public static "pushGuiLayer"(arg0: $Screen$Type): void
public static "popGuiLayer"(): void
public static "getModelData"(arg0: $BlockPos$Type): $ModelData
get "guiFarPlane"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiHooks$Type = ($GuiHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiHooks_ = $GuiHooks$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/layer/$Layer" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"

export class $Layer {

constructor(arg0: $Fullscreen$Type)

public "propagateClick"(): boolean
public "onMouseClick"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: integer, arg5: boolean, arg6: float): $List<($DrawStep)>
public "onMouseMove"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: float, arg5: boolean): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Layer$Type = ($Layer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Layer_ = $Layer$Type;
}}
declare module "packages/journeymap/client/mod/$IBlockColorProxy" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $IBlockColorProxy {

 "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
 "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}

export namespace $IBlockColorProxy {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockColorProxy$Type = ($IBlockColorProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockColorProxy_ = $IBlockColorProxy$Type;
}}
declare module "packages/journeymap/client/task/main/$MainTaskController" {
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"

export class $MainTaskController {

constructor()

public "isActive"(): boolean
public "performTasks"(): void
public "addTask"(arg0: $IMainThreadTask$Type): void
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MainTaskController$Type = ($MainTaskController);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MainTaskController_ = $MainTaskController$Type;
}}
declare module "packages/journeymap/client/thread/$RunnableTask" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $RunnableTask implements $Runnable {

constructor(arg0: $ExecutorService$Type, arg1: $ITask$Type)

public "run"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RunnableTask$Type = ($RunnableTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RunnableTask_ = $RunnableTask$Type;
}}
declare module "packages/journeymap/client/api/event/$RegistryEvent" {
import {$RegistryEvent$RegistryType, $RegistryEvent$RegistryType$Type} from "packages/journeymap/client/api/event/$RegistryEvent$RegistryType"
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"

export class $RegistryEvent extends $ClientEvent {
readonly "type": $ClientEvent$Type
readonly "dimension": $ResourceKey<($Level)>
readonly "timestamp": long


public "getRegistryType"(): $RegistryEvent$RegistryType
get "registryType"(): $RegistryEvent$RegistryType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvent$Type = ($RegistryEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvent_ = $RegistryEvent$Type;
}}
declare module "packages/journeymap/client/model/$BlockFlag" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BlockFlag extends $Enum<($BlockFlag)> {
static readonly "Ignore": $BlockFlag
static readonly "Foliage": $BlockFlag
static readonly "Grass": $BlockFlag
static readonly "Water": $BlockFlag
static readonly "Fluid": $BlockFlag
static readonly "OpenToSky": $BlockFlag
static readonly "NoShadow": $BlockFlag
static readonly "Transparency": $BlockFlag
static readonly "Error": $BlockFlag
static readonly "Plant": $BlockFlag
static readonly "Crop": $BlockFlag
static readonly "NoTopo": $BlockFlag
static readonly "Force": $BlockFlag


public static "values"(): ($BlockFlag)[]
public static "valueOf"(arg0: string): $BlockFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFlag$Type = (("error") | ("water") | ("opentosky") | ("foliage") | ("grass") | ("transparency") | ("plant") | ("noshadow") | ("ignore") | ("fluid") | ("force") | ("notopo") | ("crop")) | ($BlockFlag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFlag_ = $BlockFlag$Type;
}}
declare module "packages/journeymap/client/mod/$ModPropertyEnum" {
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $ModPropertyEnum<T> {

constructor(arg0: string, arg1: string, arg2: $Method$Type, arg3: $Class$Type<(T)>)
constructor(arg0: string, arg1: string, arg2: string, arg3: $Class$Type<(T)>, arg4: ($Class$Type<(any)>)[])
constructor(arg0: $EnumProperty$Type<(any)>, arg1: $Method$Type, arg2: $Class$Type<(T)>)
constructor(arg0: $EnumProperty$Type<(any)>, arg1: string, arg2: $Class$Type<(T)>, arg3: ($Class$Type<(any)>)[])
constructor(arg0: string, arg1: string, arg2: string, arg3: $Class$Type<(T)>)

public static "getFirstValue"<T>(arg0: $Collection$Type<($ModPropertyEnum$Type<(T)>)>, arg1: $BlockState$Type, ...arg2: (any)[]): T
public "getValue"(arg0: $BlockState$Type, ...arg1: (any)[]): T
public "isValid"(): boolean
public static "lookupPropertyEnum"(arg0: string, arg1: string): $EnumProperty<(any)>
public static "lookupMethod"(arg0: $EnumProperty$Type<(any)>, arg1: string, ...arg2: ($Class$Type<(any)>)[]): $Method
public static "lookupMethod"(arg0: string, arg1: string, ...arg2: ($Class$Type<(any)>)[]): $Method
public "getPropertyEnum"(): $EnumProperty<(any)>
get "valid"(): boolean
get "propertyEnum"(): $EnumProperty<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModPropertyEnum$Type<T> = ($ModPropertyEnum<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModPropertyEnum_<T> = $ModPropertyEnum$Type<(T)>;
}}
declare module "packages/journeymap/client/render/draw/$DrawWayPointStep" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $DrawWayPointStep implements $DrawStep {
readonly "waypoint": $Waypoint

constructor(arg0: $Waypoint$Type)
constructor(arg0: $Waypoint$Type, arg1: integer, arg2: integer, arg3: boolean)

public "getPosition"(arg0: double, arg1: double, arg2: $GridRenderer$Type, arg3: boolean): $Point2D$Double
public "getDisplayOrder"(): integer
public "isOnScreen"(): boolean
public "drawOffscreen"(arg0: $PoseStack$Type, arg1: $DrawStep$Pass$Type, arg2: $Point2D$Type, arg3: double): void
public "setShowLabel"(arg0: boolean): void
public "setLabelScale"(arg0: float): void
public "setOnScreen"(arg0: boolean): void
public "setIconScale"(arg0: float): void
public "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
public "getModId"(): string
get "displayOrder"(): integer
get "onScreen"(): boolean
set "showLabel"(value: boolean)
set "labelScale"(value: float)
set "onScreen"(value: boolean)
set "iconScale"(value: float)
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawWayPointStep$Type = ($DrawWayPointStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawWayPointStep_ = $DrawWayPointStep$Type;
}}
declare module "packages/journeymap/client/cartography/color/$ColoredSprite" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $ColoredSprite {

constructor(arg0: $TextureAtlasSprite$Type, arg1: integer)
constructor(arg0: $BakedQuad$Type)

public "getColoredImage"(): $NativeImage
public "getIconName"(): string
public "getColor"(): integer
public "hasColor"(): boolean
get "coloredImage"(): $NativeImage
get "iconName"(): string
get "color"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColoredSprite$Type = ($ColoredSprite);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColoredSprite_ = $ColoredSprite$Type;
}}
declare module "packages/journeymap/client/model/$SplashPerson" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"

export class $SplashPerson {
readonly "name": string
readonly "uuid": string
readonly "title": string
 "button": $Button
 "width": integer
 "moveX": double
 "moveY": double

constructor(arg0: string, arg1: string, arg2: string)

public "setWidth"(arg0: integer): void
public "getDistance"(arg0: $SplashPerson$Type): double
public "getSkin"(): $Texture
public "getWidth"(arg0: $Font$Type): integer
public "getButton"(): $Button
public "avoid"(arg0: $List$Type<($SplashPerson$Type)>): void
public "randomizeVector"(): void
public "continueVector"(): void
public "setButton"(arg0: $Button$Type): void
public "adjustVector"(arg0: $Rectangle2D$Double$Type): void
set "width"(value: integer)
get "skin"(): $Texture
get "button"(): $Button
set "button"(value: $Button$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SplashPerson$Type = ($SplashPerson);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SplashPerson_ = $SplashPerson$Type;
}}
declare module "packages/journeymap/client/api/util/$PluginHelper" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$IClientPlugin, $IClientPlugin$Type} from "packages/journeymap/client/api/$IClientPlugin"
import {$IClientAPI, $IClientAPI$Type} from "packages/journeymap/client/api/$IClientAPI"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PluginHelper extends $Enum<($PluginHelper)> {
static readonly "INSTANCE": $PluginHelper
static readonly "LOGGER": $Logger
static readonly "PLUGIN_ANNOTATION_NAME": $Type
static readonly "PLUGIN_INTERFACE_NAME": string


public static "values"(): ($PluginHelper)[]
public static "valueOf"(arg0: string): $PluginHelper
public "getPlugins"(): $Map<(string), ($IClientPlugin)>
public "preInitPlugins"(arg0: $List$Type<(string)>): $Map<(string), ($IClientPlugin)>
public "initPlugins"(arg0: $IClientAPI$Type): $Map<(string), ($IClientPlugin)>
get "plugins"(): $Map<(string), ($IClientPlugin)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginHelper$Type = (("instance")) | ($PluginHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginHelper_ = $PluginHelper$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$WaypointChat" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$ChatScreen, $ChatScreen$Type} from "packages/net/minecraft/client/gui/screens/$ChatScreen"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointChat extends $ChatScreen {
static readonly "MOUSE_SCROLL_SPEED": double
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Waypoint$Type)
constructor(arg0: string)

public "m_7856_"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointChat$Type = ($WaypointChat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointChat_ = $WaypointChat$Type;
}}
declare module "packages/journeymap/client/event/handlers/$DeathPointHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DeathPointHandler {

constructor()

public "handlePlayerDeath"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeathPointHandler$Type = ($DeathPointHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeathPointHandler_ = $DeathPointHandler$Type;
}}
declare module "packages/journeymap/client/model/$MapType" {
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $MapType {
readonly "vSlice": integer
readonly "name": $MapType$Name
readonly "dimension": $ResourceKey<($Level)>
readonly "apiMapType": $Context$MapType

constructor(arg0: $MapType$Name$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "from"(arg0: integer, arg1: $ResourceKey$Type<($Level$Type)>): $MapType
public static "from"(arg0: $MapType$Name$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>): $MapType
public static "from"(arg0: $MapType$Name$Type, arg1: $EntityDTO$Type): $MapType
public static "day"(arg0: $ResourceKey$Type<($Level$Type)>): $MapType
public static "day"(arg0: $EntityDTO$Type): $MapType
public "isAllowed"(): boolean
public "toCacheKey"(): string
public static "toCacheKey"(arg0: $MapType$Name$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>): string
public "isDayOrNight"(): boolean
public "isSurfaceType"(): boolean
public "isSurface"(): boolean
public static "none"(): $MapType
public "isTopo"(): boolean
public static "topo"(arg0: $EntityDTO$Type): $MapType
public static "topo"(arg0: $ResourceKey$Type<($Level$Type)>): $MapType
public "isDay"(): boolean
public "isNight"(): boolean
public "isBiome"(): boolean
public static "biome"(arg0: $ResourceKey$Type<($Level$Type)>): $MapType
public static "biome"(arg0: $EntityDTO$Type): $MapType
public "isUnderground"(): boolean
public static "underground"(arg0: integer, arg1: $ResourceKey$Type<($Level$Type)>): $MapType
public static "underground"(arg0: $EntityDTO$Type): $MapType
public static "night"(arg0: $ResourceKey$Type<($Level$Type)>): $MapType
public static "night"(arg0: $EntityDTO$Type): $MapType
public static "fromApiContextMapType"(arg0: $Context$MapType$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>): $MapType
get "allowed"(): boolean
get "dayOrNight"(): boolean
get "surfaceType"(): boolean
get "surface"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapType$Type = ($MapType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapType_ = $MapType$Type;
}}
declare module "packages/journeymap/client/waypoint/$Waypoint" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Waypoint$Type, $Waypoint$Type$Type} from "packages/journeymap/client/waypoint/$Waypoint$Type"
import {$Waypoint as $Waypoint$0, $Waypoint$Type as $Waypoint$0$Type} from "packages/journeymap/client/api/display/$Waypoint"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$WaypointGroup, $WaypointGroup$Type} from "packages/journeymap/client/waypoint/$WaypointGroup"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $Waypoint implements $Serializable {
static readonly "VERSION": integer
static readonly "GSON": $Gson

constructor(arg0: string, arg1: $BlockPos$Type, arg2: $Color$Type, arg3: $Waypoint$Type$Type, arg4: string, arg5: boolean)
constructor(arg0: string, arg1: integer, arg2: integer, arg3: integer, arg4: boolean, arg5: integer, arg6: integer, arg7: integer, arg8: $Waypoint$Type$Type, arg9: string, arg10: string, arg11: $Collection$Type<(string)>, arg12: boolean)
constructor()
constructor(arg0: $Waypoint$0$Type)
constructor(arg0: $Waypoint$Type)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $Entity$Type): $Waypoint
public "setName"(arg0: string): $Waypoint
public "getId"(): string
public "getType"(): $Waypoint$Type
public static "at"(arg0: $BlockPos$Type, arg1: $Waypoint$Type$Type, arg2: string): $Waypoint
public "getFileName"(): string
public "isDirty"(): boolean
public "setColor"(arg0: integer): $Waypoint
public "getDimensions"(): $Collection<(string)>
public "setType"(arg0: $Waypoint$Type$Type): $Waypoint
public "getPosition"(): $Vec3
public "isTeleportReady"(): boolean
public "getGuid"(): string
public static "fromString"(arg0: string): $Waypoint
public "setGroup"(arg0: $WaypointGroup$Type): $Waypoint
public "setDirty"(arg0: boolean): $Waypoint
public "setDirty"(): $Waypoint
public "isDeathPoint"(): boolean
public "isEnable"(): boolean
public "getIconColor"(): integer
public "getSafeColor"(): integer
public "showDeviation"(): boolean
public "updateId"(): string
public "setEnable"(arg0: boolean): $Waypoint
public "setOrigin"(arg0: string): $Waypoint
public "setDimensions"(arg0: $Collection$Type<(string)>): $Waypoint
public "setRandomColor"(): $Waypoint
public "getG"(): integer
public "getY"(): integer
public "getTexture"(): $Texture
public "getZ"(): integer
public "setY"(arg0: integer): void
public "getX"(): integer
public "getB"(): integer
public "getR"(): integer
public "setPersistent"(arg0: boolean): $Waypoint
public "getPrettyName"(): string
public "getIcon"(): $ResourceLocation
public "setIconColor"(arg0: integer): void
public "getDisplayId"(): string
public "setLocation"(arg0: integer, arg1: integer, arg2: integer, arg3: string): $Waypoint
public "getRawCenterX"(): double
public "getRawCenterZ"(): double
public "getBlockCenteredX"(): double
public "setGroupName"(arg0: string): $Waypoint
public "getBlockCenteredY"(): double
public "getBlockCenteredZ"(): double
public "setR"(arg0: integer): $Waypoint
public "setB"(arg0: integer): $Waypoint
public "setShowDeviation"(arg0: boolean): $Waypoint
public "setG"(arg0: integer): $Waypoint
public "getColor"(): integer
public "setIcon"(arg0: $ResourceLocation$Type): $Waypoint
public "isInPlayerDimension"(): boolean
public "toChatString"(arg0: boolean): string
public "toChatString"(): string
public "getBlockPos"(): $BlockPos
public "isPersistent"(): boolean
public "getTextureResource"(): $ResourceLocation
public "validateName"(): void
public "getOrigin"(): string
public "getGroup"(): $WaypointGroup
public static "toModWaypoint"(arg0: $Waypoint$Type): $Waypoint$0
public "modWaypoint"(): $Waypoint$0
public "getChunkCoordIntPair"(): $ChunkPos
get "name"(): string
set "name"(value: string)
get "id"(): string
get "type"(): $Waypoint$Type
get "fileName"(): string
get "dirty"(): boolean
set "color"(value: integer)
get "dimensions"(): $Collection<(string)>
set "type"(value: $Waypoint$Type$Type)
get "position"(): $Vec3
get "teleportReady"(): boolean
get "guid"(): string
set "group"(value: $WaypointGroup$Type)
set "dirty"(value: boolean)
get "deathPoint"(): boolean
get "enable"(): boolean
get "iconColor"(): integer
get "safeColor"(): integer
set "enable"(value: boolean)
set "origin"(value: string)
set "dimensions"(value: $Collection$Type<(string)>)
get "g"(): integer
get "y"(): integer
get "texture"(): $Texture
get "z"(): integer
set "y"(value: integer)
get "x"(): integer
get "b"(): integer
get "r"(): integer
set "persistent"(value: boolean)
get "prettyName"(): string
get "icon"(): $ResourceLocation
set "iconColor"(value: integer)
get "displayId"(): string
get "rawCenterX"(): double
get "rawCenterZ"(): double
get "blockCenteredX"(): double
set "groupName"(value: string)
get "blockCenteredY"(): double
get "blockCenteredZ"(): double
set "r"(value: integer)
set "b"(value: integer)
set "g"(value: integer)
get "color"(): integer
set "icon"(value: $ResourceLocation$Type)
get "inPlayerDimension"(): boolean
get "blockPos"(): $BlockPos
get "persistent"(): boolean
get "textureResource"(): $ResourceLocation
get "origin"(): string
get "group"(): $WaypointGroup
get "chunkCoordIntPair"(): $ChunkPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Waypoint$Type = ($Waypoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Waypoint_ = $Waypoint$Type;
}}
declare module "packages/journeymap/client/cartography/render/$EndSurfaceRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$SurfaceRenderer, $SurfaceRenderer$Type} from "packages/journeymap/client/cartography/render/$SurfaceRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $EndSurfaceRenderer extends $SurfaceRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor()

public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EndSurfaceRenderer$Type = ($EndSurfaceRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EndSurfaceRenderer_ = $EndSurfaceRenderer$Type;
}}
declare module "packages/journeymap/client/render/draw/$BaseOverlayDrawStep" {
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$OverlayDrawStep, $OverlayDrawStep$Type} from "packages/journeymap/client/render/draw/$OverlayDrawStep"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"

export class $BaseOverlayDrawStep<T extends $Overlay> implements $OverlayDrawStep {
readonly "overlay": T


public "setTitlePosition"(arg0: $Point2D$Double$Type): void
public "getBounds"(): $Rectangle2D$Double
public "getDisplayOrder"(): integer
public "isOnScreen"(arg0: $PoseStack$Type, arg1: double, arg2: double, arg3: $GridRenderer$Type, arg4: double): boolean
public "setEnabled"(arg0: boolean): void
public "getOverlay"(): $Overlay
public "getModId"(): string
public "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
set "titlePosition"(value: $Point2D$Double$Type)
get "bounds"(): $Rectangle2D$Double
get "displayOrder"(): integer
set "enabled"(value: boolean)
get "overlay"(): $Overlay
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseOverlayDrawStep$Type<T> = ($BaseOverlayDrawStep<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseOverlayDrawStep_<T> = $BaseOverlayDrawStep$Type<(T)>;
}}
declare module "packages/journeymap/client/model/$BlockCoordIntPair" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BlockCoordIntPair {
 "x": integer
 "z": integer

constructor()
constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "setLocation"(arg0: integer, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCoordIntPair$Type = ($BlockCoordIntPair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCoordIntPair_ = $BlockCoordIntPair$Type;
}}
declare module "packages/journeymap/client/ui/component/$DropDownItem" {
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$SelectableParent, $SelectableParent$Type} from "packages/journeymap/client/ui/component/$SelectableParent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $DropDownItem extends $Button {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $SelectableParent$Type, arg1: any, arg2: boolean, arg3: string, arg4: $Button$OnPress$Type)
constructor(arg0: $SelectableParent$Type, arg1: any, arg2: string)
constructor(arg0: $SelectableParent$Type, arg1: any, arg2: string, ...arg3: (string)[])
constructor(arg0: $SelectableParent$Type, arg1: any, arg2: string, arg3: $Button$OnPress$Type)
constructor(arg0: $SelectableParent$Type, arg1: any, arg2: string, arg3: $Button$OnPress$Type, ...arg4: (string)[])

public "getId"(): any
public "press"(): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "isHovered"(): boolean
public "getLabel"(): string
public "isAutoClose"(): boolean
public "renderSpecialDecoration"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
get "id"(): any
get "hovered"(): boolean
get "label"(): string
get "autoClose"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropDownItem$Type = ($DropDownItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropDownItem_ = $DropDownItem$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeLabelSource$InfoSlot" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ThemeLabelSource$InfoSlot {

constructor(arg0: string, arg1: long, arg2: long, arg3: $Supplier$Type<(string)>)
constructor(arg0: string, arg1: string, arg2: long, arg3: long, arg4: $Supplier$Type<(string)>)

public "getKey"(): string
public "getLabelText"(arg0: long): string
public "getTooltip"(): string
public "isShown"(): boolean
get "key"(): string
get "tooltip"(): string
get "shown"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeLabelSource$InfoSlot$Type = ($ThemeLabelSource$InfoSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeLabelSource$InfoSlot_ = $ThemeLabelSource$InfoSlot$Type;
}}
declare module "packages/journeymap/client/mod/impl/$ProjectVibrant" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"

export class $ProjectVibrant implements $IModBlockHandler {

constructor()

public "initialize"(arg0: $BlockMD$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProjectVibrant$Type = ($ProjectVibrant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProjectVibrant_ = $ProjectVibrant$Type;
}}
declare module "packages/journeymap/client/cartography/render/$TopoRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$BaseRenderer, $BaseRenderer$Type} from "packages/journeymap/client/cartography/render/$BaseRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $TopoRenderer extends $BaseRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor()

public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "getBlockHeight"(arg0: $ChunkMD$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): integer
public "getBlockHeight"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): integer
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TopoRenderer$Type = ($TopoRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TopoRenderer_ = $TopoRenderer$Type;
}}
declare module "packages/journeymap/client/service/webmap/kotlin/enums/$WebmapStatus" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WebmapStatus extends $Enum<($WebmapStatus)> {
static readonly "READY": $WebmapStatus
static readonly "DISABLED": $WebmapStatus
static readonly "NO_WORLD": $WebmapStatus
static readonly "STARTING": $WebmapStatus


public static "values"(): ($WebmapStatus)[]
public static "valueOf"(arg0: string): $WebmapStatus
public "getStatus"(): string
get "status"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WebmapStatus$Type = (("ready") | ("no_world") | ("disabled") | ("starting")) | ($WebmapStatus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WebmapStatus_ = $WebmapStatus$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$WaypointManagerItem" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$WaypointManager, $WaypointManager$Type} from "packages/journeymap/client/ui/waypoint/$WaypointManager"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Slot, $Slot$Type} from "packages/journeymap/client/ui/component/$Slot"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $WaypointManagerItem extends $Slot {

constructor(arg0: $Waypoint$Type, arg1: $Font$Type, arg2: $WaypointManager$Type)

public "contains"(arg0: $SlotMetadata$Type<(any)>): boolean
public "getDistanceTo"(arg0: $Player$Type): integer
public "getLastPressed"(): $SlotMetadata<(any)>
public "getDistance"(): integer
public "setEnabled"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "clickScrollable"(arg0: double, arg1: double): boolean
public "getColumnWidth"(): integer
public "getMetadata"(): $Collection<($SlotMetadata)>
public "getSlotIndex"(): integer
public "displayHover"(arg0: boolean): void
public "getCurrentTooltip"(): $SlotMetadata<(any)>
public "getChildSlots"(arg0: integer, arg1: integer): $List<($Slot)>
get "lastPressed"(): $SlotMetadata<(any)>
get "distance"(): integer
set "enabled"(value: boolean)
get "columnWidth"(): integer
get "metadata"(): $Collection<($SlotMetadata)>
get "slotIndex"(): integer
get "currentTooltip"(): $SlotMetadata<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointManagerItem$Type = ($WaypointManagerItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointManagerItem_ = $WaypointManagerItem$Type;
}}
declare module "packages/journeymap/client/ui/component/$ButtonList$Direction" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ButtonList$Direction extends $Enum<($ButtonList$Direction)> {
static readonly "LeftToRight": $ButtonList$Direction
static readonly "RightToLeft": $ButtonList$Direction


public static "values"(): ($ButtonList$Direction)[]
public static "valueOf"(arg0: string): $ButtonList$Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonList$Direction$Type = (("righttoleft") | ("lefttoright")) | ($ButtonList$Direction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonList$Direction_ = $ButtonList$Direction$Type;
}}
declare module "packages/journeymap/client/model/$EntityHelper$EntityMapComparator" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"

export class $EntityHelper$EntityMapComparator implements $Comparator<($EntityDTO)> {


public "compare"(arg0: $EntityDTO$Type, arg1: $EntityDTO$Type): integer
public "equals"(arg0: any): boolean
public static "reverseOrder"<T extends $Comparable<(any)>>(): $Comparator<($EntityDTO)>
public static "comparing"<T, U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($EntityDTO)>
public static "comparing"<T, U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparing"(arg0: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparing"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparing"<U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($EntityDTO)>
public static "comparingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Comparator<($EntityDTO)>
public static "comparingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Comparator<($EntityDTO)>
public static "comparingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($EntityDTO)>
public "reversed"(): $Comparator<($EntityDTO)>
public "thenComparingInt"(arg0: $ToIntFunction$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparingLong"(arg0: $ToLongFunction$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparingDouble"(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($EntityDTO)>
public static "naturalOrder"<T extends $Comparable<(any)>>(): $Comparator<($EntityDTO)>
public static "nullsFirst"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public static "nullsLast"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHelper$EntityMapComparator$Type = ($EntityHelper$EntityMapComparator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHelper$EntityMapComparator_ = $EntityHelper$EntityMapComparator$Type;
}}
declare module "packages/journeymap/client/task/main/$MappingMonitorTask" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"

export class $MappingMonitorTask implements $IMainThreadTask {

constructor()

public "getName"(): string
public "perform"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type): $IMainThreadTask
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingMonitorTask$Type = ($MappingMonitorTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingMonitorTask_ = $MappingMonitorTask$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawMarkerStep" {
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BaseOverlayDrawStep, $BaseOverlayDrawStep$Type} from "packages/journeymap/client/render/draw/$BaseOverlayDrawStep"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$MarkerOverlay, $MarkerOverlay$Type} from "packages/journeymap/client/api/display/$MarkerOverlay"

export class $DrawMarkerStep extends $BaseOverlayDrawStep<($MarkerOverlay)> {
readonly "overlay": T

constructor(arg0: $MarkerOverlay$Type)

public "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawMarkerStep$Type = ($DrawMarkerStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawMarkerStep_ = $DrawMarkerStep$Type;
}}
declare module "packages/journeymap/client/ui/component/$IntSliderButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$IConfigFieldHolder, $IConfigFieldHolder$Type} from "packages/journeymap/client/ui/component/$IConfigFieldHolder"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SliderButton, $SliderButton$Type} from "packages/journeymap/client/ui/component/$SliderButton"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $IntSliderButton extends $Button implements $IConfigFieldHolder<($IntegerField)>, $SliderButton {
 "prefix": string
 "dragging": boolean
 "minValue": integer
 "maxValue": integer
 "suffix": string
 "drawString": boolean
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $IntegerField$Type, arg1: string, arg2: string)
constructor(arg0: $IntegerField$Type, arg1: string, arg2: string, arg3: boolean)

public "getValue"(): integer
public "setValue"(arg0: integer): void
public "refresh"(): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getConfigField"(): $IntegerField
public "updateLabel"(): void
public "getFitWidth"(arg0: $Font$Type): integer
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "setSliderValue"(arg0: double): void
public "getSliderValue"(): double
get "value"(): integer
set "value"(value: integer)
get "configField"(): $IntegerField
set "sliderValue"(value: double)
get "sliderValue"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntSliderButton$Type = ($IntSliderButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntSliderButton_ = $IntSliderButton$Type;
}}
declare module "packages/journeymap/client/ui/theme/impl/$FlatTheme" {
import {$Theme$ImageSpec, $Theme$ImageSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ImageSpec"
import {$Theme$Fullscreen, $Theme$Fullscreen$Type} from "packages/journeymap/client/ui/theme/$Theme$Fullscreen"
import {$Theme$Control, $Theme$Control$Type} from "packages/journeymap/client/ui/theme/$Theme$Control"
import {$Theme$Container, $Theme$Container$Type} from "packages/journeymap/client/ui/theme/$Theme$Container"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$Theme$Minimap, $Theme$Minimap$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap"

export class $FlatTheme extends $Theme {
static readonly "VERSION": double
 "schema": integer
 "author": string
 "name": string
 "directory": string
 "container": $Theme$Container
 "control": $Theme$Control
 "fullscreen": $Theme$Fullscreen
 "icon": $Theme$ImageSpec
 "minimap": $Theme$Minimap


public static "EndCity"(): $Theme
public static "createPurist"(): $Theme
public static "createStronghold"(): $Theme
public static "createDesertTemple"(): $Theme
public static "createOceanMonument"(): $Theme
public static "createForestMansion"(): $Theme
public static "createNetherFortress"(): $Theme
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlatTheme$Type = ($FlatTheme);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlatTheme_ = $FlatTheme$Type;
}}
declare module "packages/journeymap/client/waypoint/$JmReader" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $JmReader {

constructor()

public "loadWaypoints"(arg0: $File$Type): $Collection<($Waypoint)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JmReader$Type = ($JmReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JmReader_ = $JmReader$Type;
}}
declare module "packages/journeymap/client/data/$PlayerData" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $PlayerData extends $CacheLoader<($Class), ($EntityDTO)> {

constructor()

public static "playerIsUnderground"(arg0: $Minecraft$Type, arg1: $Player$Type): boolean
public "load"(arg0: $Class$Type<(any)>): $EntityDTO
public "getTTL"(): long
get "tTL"(): long
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
declare module "packages/journeymap/client/ui/component/$ScrollPane" {
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$ObjectSelectionList, $ObjectSelectionList$Type} from "packages/net/minecraft/client/gui/components/$ObjectSelectionList"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ScrollPane$ScrollPaneEntry, $ScrollPane$ScrollPaneEntry$Type} from "packages/journeymap/client/ui/component/$ScrollPane$ScrollPaneEntry"

export class $ScrollPane extends $ObjectSelectionList<($ScrollPane$ScrollPaneEntry)> {
 "paneWidth": integer
 "paneHeight": integer
 "origin": $Point2D$Double
 "scrolling": boolean
 "hovered": E

constructor(arg0: $JmUI$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: $List$Type<(any)>, arg5: integer, arg6: integer)

public "getSlotHeight"(): integer
public "inFullView"(arg0: $Button$Type): boolean
public "setRenderSolidBackground"(arg0: boolean): void
public "setRenderDecorations"(arg0: boolean): void
public "renderItem"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: float): void
public "isScrollVisible"(): boolean
public "setDimensions"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "getY"(): integer
public "mouseClicked"(arg0: integer, arg1: integer, arg2: integer): $Button
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getX"(): integer
public "getWidth"(): integer
public "getButton"(arg0: integer, arg1: integer): $Button
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "m_7987_"(arg0: integer): boolean
public "getMaxScroll"(): integer
public "getFitWidth"(arg0: $Font$Type): integer
public "getChildAt"(arg0: double, arg1: double): $Optional<($GuiEventListener)>
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "setDrawPartialScrollable"(arg0: boolean): void
get "slotHeight"(): integer
set "renderSolidBackground"(value: boolean)
set "renderDecorations"(value: boolean)
get "scrollVisible"(): boolean
get "y"(): integer
get "x"(): integer
get "width"(): integer
get "maxScroll"(): integer
set "drawPartialScrollable"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollPane$Type = ($ScrollPane);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollPane_ = $ScrollPane$Type;
}}
declare module "packages/journeymap/client/io/$RegionImageHandler" {
import {$TileDrawStep, $TileDrawStep$Type} from "packages/journeymap/client/render/map/$TileDrawStep"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $RegionImageHandler {

constructor()

public static "getTileDrawSteps"(arg0: $File$Type, arg1: $ChunkPos$Type, arg2: $ChunkPos$Type, arg3: $MapType$Type, arg4: integer, arg5: boolean): $List<($TileDrawStep)>
public static "getMergedChunks"(arg0: $File$Type, arg1: $ChunkPos$Type, arg2: $ChunkPos$Type, arg3: $MapType$Type, arg4: boolean, arg5: $NativeImage$Type, arg6: integer, arg7: integer, arg8: boolean, arg9: boolean): $NativeImage
public static "readRegionImage"(arg0: $File$Type): $NativeImage
public static "getRegionImageFile"(arg0: $RegionCoord$Type, arg1: $MapType$Type): $File
public static "getImageDir"(arg0: $RegionCoord$Type, arg1: $MapType$Type): $File
public static "getImage"(arg0: $File$Type): $NativeImage
public static "getBlank512x512ImageFile"(): $File
get "blank512x512ImageFile"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionImageHandler$Type = ($RegionImageHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionImageHandler_ = $RegionImageHandler$Type;
}}
declare module "packages/journeymap/client/api/option/$BooleanOption" {
import {$Option, $Option$Type} from "packages/journeymap/client/api/option/$Option"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $BooleanOption extends $Option<(boolean)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: boolean)
constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: boolean, arg4: boolean)

public "isMaster"(): boolean
get "master"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanOption$Type = ($BooleanOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanOption_ = $BooleanOption$Type;
}}
declare module "packages/journeymap/client/waypoint/$WaypointGroupStore" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$WaypointGroup, $WaypointGroup$Type} from "packages/journeymap/client/waypoint/$WaypointGroup"
import {$LoadingCache, $LoadingCache$Type} from "packages/com/google/common/cache/$LoadingCache"

export class $WaypointGroupStore extends $Enum<($WaypointGroupStore)> {
static readonly "INSTANCE": $WaypointGroupStore
static readonly "KEY_PATTERN": string
static readonly "FILENAME": string
readonly "cache": $LoadingCache<(string), ($WaypointGroup)>


public "remove"(arg0: $WaypointGroup$Type): void
public "get"(arg0: string, arg1: string): $WaypointGroup
public "get"(arg0: string): $WaypointGroup
public "put"(arg0: $WaypointGroup$Type): void
public static "values"(): ($WaypointGroupStore)[]
public static "valueOf"(arg0: string): $WaypointGroupStore
public "save"(arg0: boolean): void
public "save"(): void
public "exists"(arg0: $WaypointGroup$Type): boolean
public "getFromKey"(arg0: string): $WaypointGroup
public "putIfNew"(arg0: $WaypointGroup$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointGroupStore$Type = (("instance")) | ($WaypointGroupStore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointGroupStore_ = $WaypointGroupStore$Type;
}}
declare module "packages/journeymap/client/task/multi/$InitColorManagerTask" {
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$File, $File$Type} from "packages/java/io/$File"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"

export class $InitColorManagerTask implements $ITask {

constructor()

public "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "getMaxRuntime"(): integer
get "maxRuntime"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InitColorManagerTask$Type = ($InitColorManagerTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InitColorManagerTask_ = $InitColorManagerTask$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/layer/$BlockInfoLayer" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$Layer, $Layer$Type} from "packages/journeymap/client/ui/fullscreen/layer/$Layer"

export class $BlockInfoLayer extends $Layer {

constructor(arg0: $Fullscreen$Type)

public "propagateClick"(): boolean
public "onMouseClick"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: integer, arg5: boolean, arg6: float): $List<($DrawStep)>
public "onMouseMove"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: float, arg5: boolean): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInfoLayer$Type = ($BlockInfoLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInfoLayer_ = $BlockInfoLayer$Type;
}}
declare module "packages/journeymap/client/ui/component/$ButtonList$Layout" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ButtonList$Layout extends $Enum<($ButtonList$Layout)> {
static readonly "Horizontal": $ButtonList$Layout
static readonly "Vertical": $ButtonList$Layout
static readonly "CenteredHorizontal": $ButtonList$Layout
static readonly "CenteredVertical": $ButtonList$Layout
static readonly "DistributedHorizontal": $ButtonList$Layout
static readonly "FilledHorizontal": $ButtonList$Layout


public static "values"(): ($ButtonList$Layout)[]
public static "valueOf"(arg0: string): $ButtonList$Layout
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonList$Layout$Type = (("filledhorizontal") | ("horizontal") | ("vertical") | ("centeredhorizontal") | ("distributedhorizontal") | ("centeredvertical")) | ($ButtonList$Layout);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonList$Layout_ = $ButtonList$Layout$Type;
}}
declare module "packages/journeymap/client/cartography/render/$BaseRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$BlockCoordIntPair, $BlockCoordIntPair$Type} from "packages/journeymap/client/model/$BlockCoordIntPair"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $BaseRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor()

public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
public "getBlockHeight"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): integer
public "paintBlackBlock"(arg0: $NativeImage$Type, arg1: integer, arg2: integer): integer
public "paintBlock"(arg0: $NativeImage$Type, arg1: integer, arg2: integer, arg3: integer): integer
public "getOffsetChunk"(arg0: $ChunkMD$Type, arg1: integer, arg2: integer, arg3: $BlockCoordIntPair$Type): $ChunkMD
public "paintDimOverlay"(arg0: $NativeImage$Type, arg1: integer, arg2: integer, arg3: float): void
public "paintDimOverlay"(arg0: $NativeImage$Type, arg1: $NativeImage$Type, arg2: integer, arg3: integer, arg4: float): integer
public "paintVoidBlock"(arg0: $NativeImage$Type, arg1: integer, arg2: integer): integer
public "paintClearBlock"(arg0: $NativeImage$Type, arg1: integer, arg2: integer): integer
public "paintBadBlock"(arg0: $NativeImage$Type, arg1: integer, arg2: integer, arg3: integer): void
public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseRenderer$Type = ($BaseRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseRenderer_ = $BaseRenderer$Type;
}}
declare module "packages/journeymap/client/mod/vanilla/$BedBlockProxy" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BedBlockProxy extends $Enum<($BedBlockProxy)> implements $IBlockColorProxy {
static readonly "INSTANCE": $BedBlockProxy


public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public static "values"(): ($BedBlockProxy)[]
public static "valueOf"(arg0: string): $BedBlockProxy
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BedBlockProxy$Type = (("instance")) | ($BedBlockProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BedBlockProxy_ = $BedBlockProxy$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$OptionsManager" {
import {$OptionScreen, $OptionScreen$Type} from "packages/journeymap/client/ui/option/$OptionScreen"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $OptionsManager extends $OptionScreen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: string, arg1: $Screen$Type)
constructor(arg0: $Screen$Type, ...arg1: ($Category$Type)[])
constructor(arg0: $Screen$Type)
constructor()

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "m_7856_"(): void
public "refreshMinimapOptions"(): void
public "previewMiniMap"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsManager$Type = ($OptionsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsManager_ = $OptionsManager$Type;
}}
declare module "packages/journeymap/client/data/$AnimalsData" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnimalsData extends $CacheLoader<($Class), ($Map<(string), ($EntityDTO)>)> {

constructor()

public "load"(arg0: $Class$Type<(any)>): $Map<(string), ($EntityDTO)>
public "getTTL"(): long
get "tTL"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimalsData$Type = ($AnimalsData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimalsData_ = $AnimalsData$Type;
}}
declare module "packages/journeymap/client/ui/option/$OptionSlotFactory" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$CategorySlot, $CategorySlot$Type} from "packages/journeymap/client/ui/option/$CategorySlot"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $OptionSlotFactory {

constructor()

public static "getOptionSlots"(arg0: $Map$Type<($Category$Type), ($List$Type<($SlotMetadata$Type)>)>, arg1: $Map$Type<($Category$Type), ($PropertiesBase$Type)>): $List<($CategorySlot)>
public static "getOptionSlots"(arg0: $Map$Type<($Category$Type), ($List$Type<($SlotMetadata$Type)>)>, arg1: $Map$Type<($Category$Type), ($PropertiesBase$Type)>, arg2: boolean, arg3: boolean): $List<($CategorySlot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionSlotFactory$Type = ($OptionSlotFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionSlotFactory_ = $OptionSlotFactory$Type;
}}
declare module "packages/journeymap/client/task/main/$SoftResetTask" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"

export class $SoftResetTask implements $IMainThreadTask {


public "getName"(): string
public static "queue"(): void
public "perform"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type): $IMainThreadTask
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoftResetTask$Type = ($SoftResetTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoftResetTask_ = $SoftResetTask$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Container$Toolbar" {
import {$Theme$Container$Toolbar$ToolbarSpec, $Theme$Container$Toolbar$ToolbarSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Container$Toolbar$ToolbarSpec"

export class $Theme$Container$Toolbar {
 "horizontal": $Theme$Container$Toolbar$ToolbarSpec
 "vertical": $Theme$Container$Toolbar$ToolbarSpec

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Container$Toolbar$Type = ($Theme$Container$Toolbar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Container$Toolbar_ = $Theme$Container$Toolbar$Type;
}}
declare module "packages/journeymap/client/model/$EntityHelper$EntityDistanceComparator" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityHelper$EntityDistanceComparator implements $Comparator<($Entity)> {


public "compare"(arg0: $Entity$Type, arg1: $Entity$Type): integer
public "equals"(arg0: any): boolean
public static "reverseOrder"<T extends $Comparable<(any)>>(): $Comparator<($Entity)>
public static "comparing"<T, U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($Entity)>
public static "comparing"<T, U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($Entity)>
public "thenComparing"(arg0: $Comparator$Type<(any)>): $Comparator<($Entity)>
public "thenComparing"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($Entity)>
public "thenComparing"<U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($Entity)>
public static "comparingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Comparator<($Entity)>
public static "comparingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Comparator<($Entity)>
public static "comparingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($Entity)>
public "reversed"(): $Comparator<($Entity)>
public "thenComparingInt"(arg0: $ToIntFunction$Type<(any)>): $Comparator<($Entity)>
public "thenComparingLong"(arg0: $ToLongFunction$Type<(any)>): $Comparator<($Entity)>
public "thenComparingDouble"(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($Entity)>
public static "naturalOrder"<T extends $Comparable<(any)>>(): $Comparator<($Entity)>
public static "nullsFirst"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($Entity)>
public static "nullsLast"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($Entity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHelper$EntityDistanceComparator$Type = ($EntityHelper$EntityDistanceComparator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHelper$EntityDistanceComparator_ = $EntityHelper$EntityDistanceComparator$Type;
}}
declare module "packages/journeymap/client/texture/$SimpleTextureImpl" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$SimpleTexture, $SimpleTexture$Type} from "packages/net/minecraft/client/renderer/texture/$SimpleTexture"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SimpleTextureImpl extends $SimpleTexture implements $Texture {
static readonly "NOT_ASSIGNED": integer

constructor(arg0: $ResourceLocation$Type)

public "remove"(): void
public "getLocation"(): $ResourceLocation
public "close"(): void
public "release"(): void
public "resize"(arg0: float): void
public "getWidth"(): integer
public "getHeight"(): integer
public "setDisplayHeight"(arg0: integer): void
public "getNativeImage"(): $NativeImage
public "getScaledImage"(arg0: float): $Texture
public "setNativeImage"(arg0: $NativeImage$Type): void
public "setDisplayWidth"(arg0: integer): void
public "getTextureId"(): integer
public "getRGB"(arg0: integer, arg1: integer): integer
public "getAlpha"(): float
public "setAlpha"(arg0: float): void
public "hasImage"(): boolean
public "setNativeImage"(arg0: $NativeImage$Type, arg1: boolean): void
get "location"(): $ResourceLocation
get "width"(): integer
get "height"(): integer
set "displayHeight"(value: integer)
get "nativeImage"(): $NativeImage
set "nativeImage"(value: $NativeImage$Type)
set "displayWidth"(value: integer)
get "textureId"(): integer
get "alpha"(): float
set "alpha"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleTextureImpl$Type = ($SimpleTextureImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleTextureImpl_ = $SimpleTextureImpl$Type;
}}
declare module "packages/journeymap/client/texture/$RegionTexture$Listener" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"

export interface $RegionTexture$Listener<T extends $Texture> {

 "textureImageUpdated"(arg0: T): void

(arg0: T): void
}

export namespace $RegionTexture$Listener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionTexture$Listener$Type<T> = ($RegionTexture$Listener<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionTexture$Listener_<T> = $RegionTexture$Listener$Type<(T)>;
}}
declare module "packages/journeymap/client/ui/dialog/$AboutDialog" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AboutDialog extends $JmUI {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $JmUI$Type)

public "m_7856_"(): void
public "charTyped"(arg0: character, arg1: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AboutDialog$Type = ($AboutDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AboutDialog_ = $AboutDialog$Type;
}}
declare module "packages/journeymap/client/model/$BlockDataArrays" {
import {$BlockDataArrays$Dataset, $BlockDataArrays$Dataset$Type} from "packages/journeymap/client/model/$BlockDataArrays$Dataset"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"

export class $BlockDataArrays {

constructor()

public "get"(arg0: $MapType$Type): $BlockDataArrays$Dataset
public "clearAll"(): void
public static "areIdentical"(arg0: ((integer)[])[], arg1: ((integer)[])[]): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDataArrays$Type = ($BlockDataArrays);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDataArrays_ = $BlockDataArrays$Type;
}}
declare module "packages/journeymap/client/mod/$IBlockSpritesProxy" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ColoredSprite, $ColoredSprite$Type} from "packages/journeymap/client/cartography/color/$ColoredSprite"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $IBlockSpritesProxy {

 "getSprites"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): $Collection<($ColoredSprite)>

(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): $Collection<($ColoredSprite)>
}

export namespace $IBlockSpritesProxy {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockSpritesProxy$Type = ($IBlockSpritesProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockSpritesProxy_ = $IBlockSpritesProxy$Type;
}}
declare module "packages/journeymap/client/io/$IconSetFileHandler" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $IconSetFileHandler {
static readonly "MOB_ICON_SET_DEFAULT": string

constructor()

public static "initialize"(): void
public static "ensureEntityIconSet"(arg0: string): void
public static "ensureEntityIconSet"(arg0: string, arg1: boolean): void
public static "getIconSetNames"(arg0: $File$Type, arg1: $List$Type<(string)>): $ArrayList<(string)>
public static "getEntityIconDir"(): $File
public static "registerEntityIconDirectory"(arg0: $ResourceLocation$Type): boolean
public static "getEntityIconSetNames"(): $ArrayList<(string)>
get "entityIconDir"(): $File
get "entityIconSetNames"(): $ArrayList<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IconSetFileHandler$Type = ($IconSetFileHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IconSetFileHandler_ = $IconSetFileHandler$Type;
}}
declare module "packages/journeymap/client/cartography/render/$CaveRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$SurfaceRenderer, $SurfaceRenderer$Type} from "packages/journeymap/client/cartography/render/$SurfaceRenderer"
import {$BaseRenderer, $BaseRenderer$Type} from "packages/journeymap/client/cartography/render/$BaseRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $CaveRenderer extends $BaseRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor(arg0: $SurfaceRenderer$Type)

public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "getBlockHeight"(arg0: $ChunkMD$Type, arg1: $BlockPos$Type): integer
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaveRenderer$Type = ($CaveRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaveRenderer_ = $CaveRenderer$Type;
}}
declare module "packages/journeymap/client/mod/vanilla/$MaterialBlockColorProxy" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $MaterialBlockColorProxy implements $IBlockColorProxy {

constructor()

public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaterialBlockColorProxy$Type = ($MaterialBlockColorProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaterialBlockColorProxy_ = $MaterialBlockColorProxy$Type;
}}
declare module "packages/journeymap/client/event/handlers/$WaypointBeaconHandler" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"

export class $WaypointBeaconHandler {

constructor()

public "onRenderWaypoints"(arg0: $PoseStack$Type, arg1: boolean): void
public "onRenderWaypoints"(arg0: $PoseStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointBeaconHandler$Type = ($WaypointBeaconHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointBeaconHandler_ = $WaypointBeaconHandler$Type;
}}
declare module "packages/journeymap/client/command/$JMCommand" {
import {$CommandSource, $CommandSource$Type} from "packages/net/minecraft/commands/$CommandSource"

/**
 * 
 * @deprecated
 */
export interface $JMCommand {

 "getName"(): string
 "execute"(arg0: $CommandSource$Type, arg1: (string)[]): integer
 "getUsage"(arg0: $CommandSource$Type): string
}

export namespace $JMCommand {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMCommand$Type = ($JMCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMCommand_ = $JMCommand$Type;
}}
declare module "packages/journeymap/client/render/map/$Tile" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"

export class $Tile {
static readonly "TILESIZE": integer
static readonly "LOAD_RADIUS": integer


public static "tileToBlock"(arg0: integer, arg1: integer): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clear"(): void
public static "create"(arg0: integer, arg1: integer, arg2: integer, arg3: $File$Type, arg4: $MapType$Type, arg5: boolean): $Tile
public "cacheKey"(): string
public static "toCacheKey"(arg0: integer, arg1: integer, arg2: integer): string
public "updateTexture"(arg0: $File$Type, arg1: $MapType$Type, arg2: boolean): boolean
public "blockPixelOffsetInTile"(arg0: double, arg1: double): $Point2D
public "hasTexture"(arg0: $MapType$Type): boolean
public static "blockPosToTile"(arg0: integer, arg1: integer): integer
public static "switchTileDisplayQuality"(): void
public static "switchTileRenderType"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tile$Type = ($Tile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tile_ = $Tile$Type;
}}
declare module "packages/journeymap/client/task/main/$IMainThreadTask" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"

export interface $IMainThreadTask {

 "getName"(): string
 "perform"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type): $IMainThreadTask
}

export namespace $IMainThreadTask {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMainThreadTask$Type = ($IMainThreadTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMainThreadTask_ = $IMainThreadTask$Type;
}}
declare module "packages/journeymap/client/texture/$TextureCache" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $TextureCache {
static readonly "GridCheckers": $ResourceLocation
static readonly "GridDots": $ResourceLocation
static readonly "GridSquares": $ResourceLocation
static readonly "GridRegionSquares": $ResourceLocation
static readonly "GridRegion": $ResourceLocation
static readonly "ColorPicker": $ResourceLocation
static readonly "ColorPicker2": $ResourceLocation
static readonly "TileSampleDay": $ResourceLocation
static readonly "TileSampleNight": $ResourceLocation
static readonly "TileSampleUnderground": $ResourceLocation
static readonly "UnknownEntity": $ResourceLocation
static readonly "Deathpoint": $ResourceLocation
static readonly "MobDot": $ResourceLocation
static readonly "MobDotArrow": $ResourceLocation
static readonly "MobDotChevron": $ResourceLocation
static readonly "MobIconArrow": $ResourceLocation
static readonly "PlayerArrow": $ResourceLocation
static readonly "PlayerArrowBG": $ResourceLocation
static readonly "Logo": $ResourceLocation
static readonly "MinimapSquare128": $ResourceLocation
static readonly "MinimapSquare256": $ResourceLocation
static readonly "MinimapSquare512": $ResourceLocation
static readonly "Discord": $ResourceLocation
static readonly "Waypoint": $ResourceLocation
static readonly "WaypointEdit": $ResourceLocation
static readonly "WaypointOffscreen": $ResourceLocation
static readonly "modTextureMap": $Map<($ResourceLocation), ($ResourceLocation)>
static readonly "playerSkins": $Map<(string), ($Texture)>
static readonly "themeImages": $Map<(string), ($Texture)>
static readonly "waypointIconCache": $Map<(string), ($ResourceLocation)>

constructor()

public static "reset"(): void
public static "resolveImage"(arg0: $ResourceLocation$Type): $NativeImage
public static "uiImage"(arg0: string): $ResourceLocation
public static "getWaypointIcon"(arg0: $ResourceLocation$Type): $Texture
public static "getScaledCopy"(arg0: string, arg1: $Texture$Type, arg2: integer, arg3: integer, arg4: float): $Texture
public static "getTexture"(arg0: string): $ResourceLocation
public static "getTexture"(arg0: $ResourceLocation$Type): $Texture
public static "coloredImageResource"(arg0: $ResourceLocation$Type, arg1: integer): $ResourceLocation
public static "getSizedThemeTexture"(arg0: $Theme$Type, arg1: string, arg2: integer, arg3: integer, arg4: boolean, arg5: float): $Texture
public static "scheduleTextureTask"<T extends $Texture>(arg0: $Callable$Type<(T)>): $Future<(T)>
public static "purgeThemeImages"(arg0: $Map$Type<(string), ($Texture$Type)>): void
public static "getThemeTexture"(arg0: $Theme$Type, arg1: string): $Texture
public static "getPlayerSkin"(arg0: $UUID$Type, arg1: string): $Texture
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureCache$Type = ($TextureCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureCache_ = $TextureCache$Type;
}}
declare module "packages/journeymap/client/model/$MapType$Name" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MapType$Name extends $Enum<($MapType$Name)> {
static readonly "day": $MapType$Name
static readonly "night": $MapType$Name
static readonly "underground": $MapType$Name
static readonly "surface": $MapType$Name
static readonly "topo": $MapType$Name
static readonly "biome": $MapType$Name
static readonly "none": $MapType$Name


public static "values"(): ($MapType$Name)[]
public static "valueOf"(arg0: string): $MapType$Name
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapType$Name$Type = (("surface") | ("underground") | ("biome") | ("night") | ("none") | ("topo") | ("day")) | ($MapType$Name);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapType$Name_ = $MapType$Name$Type;
}}
declare module "packages/journeymap/client/data/$MobsData" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MobsData extends $CacheLoader<($Class), ($Map<(string), ($EntityDTO)>)> {

constructor()

public "load"(arg0: $Class$Type<(any)>): $Map<(string), ($EntityDTO)>
public "getTTL"(): long
get "tTL"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobsData$Type = ($MobsData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobsData_ = $MobsData$Type;
}}
declare module "packages/journeymap/client/api/impl/$PluginWrapper" {
import {$DisplayType, $DisplayType$Type} from "packages/journeymap/client/api/display/$DisplayType"
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/api/display/$Waypoint"
import {$OverlayDrawStep, $OverlayDrawStep$Type} from "packages/journeymap/client/render/draw/$OverlayDrawStep"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Displayable, $Displayable$Type} from "packages/journeymap/client/api/display/$Displayable"
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$IClientPlugin, $IClientPlugin$Type} from "packages/journeymap/client/api/$IClientPlugin"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $PluginWrapper {

constructor(arg0: $IClientPlugin$Type)

public "remove"(arg0: $Displayable$Type): void
public "remove"(arg0: $Waypoint$Type): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "notify"(arg0: $ClientEvent$Type): void
public "removeAll"(arg0: $DisplayType$Type): void
public "removeAll"(): void
public "exists"(arg0: $Displayable$Type): boolean
public "getWaypoint"(arg0: string): $Waypoint
public "getWaypoints"(): $List<($Waypoint)>
public "subscribe"(arg0: $EnumSet$Type<($ClientEvent$Type$Type)>): void
public "getSubscribedClientEventTypes"(): $EnumSet<($ClientEvent$Type)>
public "show"(arg0: $Displayable$Type): void
public "getDrawSteps"(arg0: $List$Type<($OverlayDrawStep$Type)>, arg1: $UIState$Type): void
get "waypoints"(): $List<($Waypoint)>
get "subscribedClientEventTypes"(): $EnumSet<($ClientEvent$Type)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginWrapper$Type = ($PluginWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginWrapper_ = $PluginWrapper$Type;
}}
declare module "packages/journeymap/client/api/option/$OptionCategory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $OptionCategory {

constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string, arg1: string)

public "toString"(): string
public "getLabel"(): string
public "getToolTip"(): string
public "getModId"(): string
get "label"(): string
get "toolTip"(): string
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionCategory$Type = ($OptionCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionCategory_ = $OptionCategory$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$PopupMenuEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$PopupMenuEvent$Layer, $PopupMenuEvent$Layer$Type} from "packages/journeymap/client/api/event/forge/$PopupMenuEvent$Layer"
import {$ModPopupMenu, $ModPopupMenu$Type} from "packages/journeymap/client/api/display/$ModPopupMenu"

export class $PopupMenuEvent extends $Event {

constructor(arg0: $ModPopupMenu$Type, arg1: $PopupMenuEvent$Layer$Type, arg2: $IFullscreen$Type)
constructor()

public "getLayer"(): $PopupMenuEvent$Layer
public "isCancelable"(): boolean
public "getFullscreen"(): $IFullscreen
public "getPopupMenu"(): $ModPopupMenu
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "layer"(): $PopupMenuEvent$Layer
get "cancelable"(): boolean
get "fullscreen"(): $IFullscreen
get "popupMenu"(): $ModPopupMenu
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuEvent$Type = ($PopupMenuEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuEvent_ = $PopupMenuEvent$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Control$ButtonSpec" {
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"

export class $Theme$Control$ButtonSpec {
 "useThemeImages": boolean
 "width": integer
 "height": integer
 "prefix": string
 "tooltipOnStyle": string
 "tooltipOffStyle": string
 "tooltipDisabledStyle": string
 "iconOn": $Theme$ColorSpec
 "iconOff": $Theme$ColorSpec
 "iconHoverOn": $Theme$ColorSpec
 "iconHoverOff": $Theme$ColorSpec
 "iconDisabled": $Theme$ColorSpec
 "buttonOn": $Theme$ColorSpec
 "buttonOff": $Theme$ColorSpec
 "buttonHoverOn": $Theme$ColorSpec
 "buttonHoverOff": $Theme$ColorSpec
 "buttonDisabled": $Theme$ColorSpec

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Control$ButtonSpec$Type = ($Theme$Control$ButtonSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Control$ButtonSpec_ = $Theme$Control$ButtonSpec$Type;
}}
declare module "packages/journeymap/client/api/model/$IFullscreen" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export interface $IFullscreen {

 "close"(): void
 "getMinecraft"(): $Minecraft
 "zoomIn"(): void
 "zoomOut"(): void
 "toggleMapType"(): void
 "getScreen"(): $Screen
 "getUiState"(): $UIState
 "updateMapType"(arg0: $Context$MapType$Type, arg1: integer, arg2: $ResourceKey$Type<($Level$Type)>): void
 "centerOn"(arg0: double, arg1: double): void
}

export namespace $IFullscreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFullscreen$Type = ($IFullscreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFullscreen_ = $IFullscreen$Type;
}}
declare module "packages/journeymap/client/properties/$ClientPropertiesBase" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"

export class $ClientPropertiesBase extends $PropertiesBase {

constructor()

public "getFile"(): $File
public "getFileName"(): string
public "isValid"(arg0: boolean): boolean
public "getHeaders"(): (string)[]
public "getCategoryByName"(arg0: string): $Category
public "updateFrom"<T extends $PropertiesBase>(arg0: T): void
public "copyToWorldConfig"(arg0: boolean): boolean
public "isWorldConfig"(): boolean
public "copyToStandardConfig"(): boolean
get "file"(): $File
get "fileName"(): string
get "headers"(): (string)[]
get "worldConfig"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPropertiesBase$Type = ($ClientPropertiesBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPropertiesBase_ = $ClientPropertiesBase$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$Shape" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $Shape extends $Enum<($Shape)> implements $KeyedEnum {
static readonly "Square": $Shape
static readonly "Rectangle": $Shape
static readonly "Circle": $Shape
readonly "key": string


public "toString"(): string
public static "values"(): ($Shape)[]
public static "valueOf"(arg0: string): $Shape
public "getKey"(): string
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Shape$Type = (("square") | ("rectangle") | ("circle")) | ($Shape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Shape_ = $Shape$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeChatEvents" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$ClientChatReceivedEvent, $ClientChatReceivedEvent$Type} from "packages/net/minecraftforge/client/event/$ClientChatReceivedEvent"
import {$ChatEventHandler, $ChatEventHandler$Type} from "packages/journeymap/client/event/handlers/$ChatEventHandler"

export class $ForgeChatEvents implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "invoke"(arg0: $ClientChatReceivedEvent$Type): void
public "getHandler"(): $ChatEventHandler
public "onChatEvent"(arg0: string): boolean
get "handler"(): $ChatEventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeChatEvents$Type = ($ForgeChatEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeChatEvents_ = $ForgeChatEvents$Type;
}}
declare module "packages/journeymap/client/api/display/$Displayable" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$DisplayType, $DisplayType$Type} from "packages/journeymap/client/api/display/$DisplayType"

export class $Displayable implements $Comparable<($Displayable)> {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compareTo"(arg0: $Displayable$Type): integer
public "getId"(): string
public static "clampRGB"(arg0: integer): integer
public static "clampOpacity"(arg0: float): float
public "getDisplayOrder"(): integer
public "getGuid"(): string
public "getDisplayType"(): $DisplayType
public "getModId"(): string
get "id"(): string
get "displayOrder"(): integer
get "guid"(): string
get "displayType"(): $DisplayType
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Displayable$Type = ($Displayable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Displayable_ = $Displayable$Type;
}}
declare module "packages/journeymap/client/data/$MessagesData" {
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MessagesData extends $CacheLoader<($Class), ($Map<(string), (any)>)> {

constructor()

public "load"(arg0: $Class$Type<(any)>): $Map<(string), (any)>
public "getTTL"(): long
get "tTL"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessagesData$Type = ($MessagesData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessagesData_ = $MessagesData$Type;
}}
declare module "packages/journeymap/client/api/option/$EnumOption" {
import {$Option, $Option$Type} from "packages/journeymap/client/api/option/$Option"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $EnumOption<E extends $KeyedEnum> extends $Option<(E)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: E)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumOption$Type<E> = ($EnumOption<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumOption_<E> = $EnumOption$Type<(E)>;
}}
declare module "packages/journeymap/client/data/$VillagersData" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VillagersData extends $CacheLoader<($Class), ($Map<(string), ($EntityDTO)>)> {

constructor()

public "load"(arg0: $Class$Type<(any)>): $Map<(string), ($EntityDTO)>
public "getTTL"(): long
get "tTL"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagersData$Type = ($VillagersData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagersData_ = $VillagersData$Type;
}}
declare module "packages/journeymap/client/api/model/$MapPolygon" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $MapPolygon {

constructor(...arg0: ($BlockPos$Type)[])
constructor(arg0: $List$Type<($BlockPos$Type)>)

public "toString"(): string
public "iterator"(): $Iterator<($BlockPos)>
public "getPoints"(): $List<($BlockPos)>
public "setPoints"(arg0: $List$Type<($BlockPos$Type)>): $MapPolygon
get "points"(): $List<($BlockPos)>
set "points"(value: $List$Type<($BlockPos$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapPolygon$Type = ($MapPolygon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapPolygon_ = $MapPolygon$Type;
}}
declare module "packages/journeymap/client/ui/component/$OnOffButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$OnOffButton$ToggleListener, $OnOffButton$ToggleListener$Type} from "packages/journeymap/client/ui/component/$OnOffButton$ToggleListener"

export class $OnOffButton extends $Button {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: string, arg1: string, arg2: boolean, arg3: $Button$OnPress$Type)

public "isActive"(): boolean
public "setLabels"(arg0: string, arg1: string): void
public "setToggled"(arg0: boolean): void
public "setToggled"(arg0: boolean, arg1: boolean): void
public "getFitWidth"(arg0: $Font$Type): integer
public "addToggleListener"(arg0: $OnOffButton$ToggleListener$Type): void
public "getToggled"(): boolean
public "toggle"(): void
get "active"(): boolean
set "toggled"(value: boolean)
get "toggled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OnOffButton$Type = ($OnOffButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OnOffButton_ = $OnOffButton$Type;
}}
declare module "packages/journeymap/client/model/$EntityHelper$EntityDTODistanceComparator" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"

export class $EntityHelper$EntityDTODistanceComparator implements $Comparator<($EntityDTO)> {


public "compare"(arg0: $EntityDTO$Type, arg1: $EntityDTO$Type): integer
public "equals"(arg0: any): boolean
public static "reverseOrder"<T extends $Comparable<(any)>>(): $Comparator<($EntityDTO)>
public static "comparing"<T, U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($EntityDTO)>
public static "comparing"<T, U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparing"(arg0: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparing"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparing"<U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($EntityDTO)>
public static "comparingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Comparator<($EntityDTO)>
public static "comparingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Comparator<($EntityDTO)>
public static "comparingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($EntityDTO)>
public "reversed"(): $Comparator<($EntityDTO)>
public "thenComparingInt"(arg0: $ToIntFunction$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparingLong"(arg0: $ToLongFunction$Type<(any)>): $Comparator<($EntityDTO)>
public "thenComparingDouble"(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($EntityDTO)>
public static "naturalOrder"<T extends $Comparable<(any)>>(): $Comparator<($EntityDTO)>
public static "nullsFirst"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
public static "nullsLast"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($EntityDTO)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHelper$EntityDTODistanceComparator$Type = ($EntityHelper$EntityDTODistanceComparator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHelper$EntityDTODistanceComparator_ = $EntityHelper$EntityDTODistanceComparator$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawStep" {
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"

export interface $DrawStep {

 "getDisplayOrder"(): integer
 "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
 "getModId"(): string
}

export namespace $DrawStep {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawStep$Type = ($DrawStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawStep_ = $DrawStep$Type;
}}
declare module "packages/journeymap/client/event/handlers/keymapping/$KeyModifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyConflictContext, $KeyConflictContext$Type} from "packages/journeymap/client/event/handlers/keymapping/$KeyConflictContext"
import {$KeyModifier as $KeyModifier$0, $KeyModifier$Type as $KeyModifier$0$Type} from "packages/net/minecraftforge/client/settings/$KeyModifier"

export class $KeyModifier extends $Enum<($KeyModifier)> {
static readonly "NONE": $KeyModifier
static readonly "CONTROL": $KeyModifier
static readonly "SHIFT": $KeyModifier
static readonly "ALT": $KeyModifier


public static "values"(): ($KeyModifier)[]
public static "valueOf"(arg0: string): $KeyModifier
public "isActive"(arg0: $KeyConflictContext$Type): boolean
public "getForge"(): $KeyModifier$0
public static "fromForge"(arg0: $KeyModifier$0$Type): $KeyModifier
get "forge"(): $KeyModifier$0
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyModifier$Type = (("shift") | ("alt") | ("control") | ("none")) | ($KeyModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyModifier_ = $KeyModifier$Type;
}}
declare module "packages/journeymap/client/api/event/$ClientEvent$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ClientEvent$Type extends $Enum<($ClientEvent$Type)> {
static readonly "DISPLAY_UPDATE": $ClientEvent$Type
static readonly "DEATH_WAYPOINT": $ClientEvent$Type
static readonly "MAPPING_STARTED": $ClientEvent$Type
static readonly "MAPPING_STOPPED": $ClientEvent$Type
static readonly "MAP_CLICKED": $ClientEvent$Type
static readonly "MAP_DRAGGED": $ClientEvent$Type
static readonly "MAP_MOUSE_MOVED": $ClientEvent$Type
static readonly "REGISTRY": $ClientEvent$Type
static readonly "WAYPOINT": $ClientEvent$Type
readonly "cancellable": boolean


public static "values"(): ($ClientEvent$Type)[]
public static "valueOf"(arg0: string): $ClientEvent$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEvent$Type$Type = (("mapping_started") | ("waypoint") | ("registry") | ("display_update") | ("map_mouse_moved") | ("mapping_stopped") | ("map_clicked") | ("map_dragged") | ("death_waypoint")) | ($ClientEvent$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEvent$Type_ = $ClientEvent$Type$Type;
}}
declare module "packages/journeymap/client/ui/component/$Button$HoverState" {
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"

export interface $Button$HoverState {

 "onHoverState"(arg0: $Button$Type, arg1: boolean): void

(arg0: $Button$Type, arg1: boolean): void
}

export namespace $Button$HoverState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Button$HoverState$Type = ($Button$HoverState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Button$HoverState_ = $Button$HoverState$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$Position" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $Position extends $Enum<($Position)> implements $KeyedEnum {
static readonly "TopRight": $Position
static readonly "BottomRight": $Position
static readonly "BottomLeft": $Position
static readonly "TopLeft": $Position
static readonly "TopCenter": $Position
static readonly "Center": $Position
static readonly "Custom": $Position
readonly "key": string


public "toString"(): string
public static "values"(): ($Position)[]
public static "valueOf"(arg0: string): $Position
public "getKey"(): string
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Position$Type = (("bottomleft") | ("topright") | ("topleft") | ("center") | ("custom") | ("bottomright") | ("topcenter")) | ($Position);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Position_ = $Position$Type;
}}
declare module "packages/journeymap/client/command/$CmdTeleportWaypoint" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/waypoint/$Waypoint"

export class $CmdTeleportWaypoint {

constructor(arg0: $Waypoint$Type)

public "run"(): void
public static "isPermitted"(arg0: $Minecraft$Type): boolean
public static "teleport"(arg0: double, arg1: integer, arg2: double, arg3: string): void
public static "teleport"(arg0: $BlockPos$Type, arg1: $ResourceKey$Type<($Level$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdTeleportWaypoint$Type = ($CmdTeleportWaypoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdTeleportWaypoint_ = $CmdTeleportWaypoint$Type;
}}
declare module "packages/journeymap/client/ui/dialog/$FullscreenActions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FullscreenActions {

constructor()

public static "openKeybindings"(): void
public static "tweet"(arg0: string): void
public static "open"(): void
public static "toggleSearchBar"(): void
public static "showCaveLayers"(): void
public static "launchLocalhost"(): void
public static "launchDownloadWebsite"(): void
public static "discord"(): void
public static "launchWebsite"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenActions$Type = ($FullscreenActions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenActions_ = $FullscreenActions$Type;
}}
declare module "packages/journeymap/client/mod/impl/$Pixelmon" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $Pixelmon implements $IModBlockHandler {
static "INSTANCE": $Pixelmon
static "loaded": boolean

constructor()

public "initialize"(arg0: $BlockMD$Type): void
public "getPixelmonResource"(arg0: $Entity$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pixelmon$Type = ($Pixelmon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pixelmon_ = $Pixelmon$Type;
}}
declare module "packages/journeymap/client/properties/$FullMapProperties" {
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$StringField, $StringField$Type} from "packages/journeymap/common/properties/config/$StringField"
import {$EntityDisplay, $EntityDisplay$Type} from "packages/journeymap/client/ui/minimap/$EntityDisplay"
import {$FloatField, $FloatField$Type} from "packages/journeymap/common/properties/config/$FloatField"
import {$InGameMapProperties, $InGameMapProperties$Type} from "packages/journeymap/client/properties/$InGameMapProperties"

export class $FullMapProperties extends $InGameMapProperties {
readonly "showKeys": $BooleanField
readonly "showPlayerLoc": $BooleanField
readonly "showMouseLoc": $BooleanField
readonly "playerDisplay": $EnumField<($EntityDisplay)>
readonly "selfDisplayScale": $FloatField
readonly "playerDisplayScale": $FloatField
readonly "showPlayerHeading": $BooleanField
readonly "mobDisplay": $EnumField<($EntityDisplay)>
readonly "mobDisplayScale": $FloatField
readonly "showMobHeading": $BooleanField
readonly "showMobs": $BooleanField
readonly "showAnimals": $BooleanField
readonly "showVillagers": $BooleanField
readonly "showPets": $BooleanField
readonly "showPlayers": $BooleanField
readonly "fontScale": $FloatField
readonly "showWaypointLabels": $BooleanField
readonly "waypointLabelScale": $FloatField
readonly "waypointIconScale": $FloatField
readonly "locationFormatVerbose": $BooleanField
readonly "locationFormat": $StringField
readonly "showWaypoints": $BooleanField
readonly "showSelf": $BooleanField
readonly "showGrid": $BooleanField
readonly "showCaves": $BooleanField
readonly "showEntityNames": $BooleanField
readonly "preferredMapType": $EnumField<($MapType$Name)>
readonly "zoomLevel": $IntegerField

constructor()

public "getName"(): string
public "postLoad"(arg0: boolean): void
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullMapProperties$Type = ($FullMapProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullMapProperties_ = $FullMapProperties$Type;
}}
declare module "packages/journeymap/client/event/handlers/$TextureAtlasHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TextureAtlasHandler {

constructor()

public "onTextureStitched"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureAtlasHandler$Type = ($TextureAtlasHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureAtlasHandler_ = $TextureAtlasHandler$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$DimensionsButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $DimensionsButton extends $Button {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Button$OnPress$Type)

public "setDim"(arg0: $ResourceKey$Type<($Level$Type)>): void
public "getFitWidth"(arg0: $Font$Type): integer
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "onPress"(): void
set "dim"(value: $ResourceKey$Type<($Level$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionsButton$Type = ($DimensionsButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionsButton_ = $DimensionsButton$Type;
}}
declare module "packages/journeymap/client/texture/$Texture" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $Texture {

 "remove"(): void
 "getLocation"(): $ResourceLocation
 "release"(): void
 "getWidth"(): integer
 "getHeight"(): integer
 "setDisplayHeight"(arg0: integer): void
 "getNativeImage"(): $NativeImage
 "getScaledImage"(arg0: float): $Texture
 "setNativeImage"(arg0: $NativeImage$Type): void
 "setNativeImage"(arg0: $NativeImage$Type, arg1: boolean): void
 "setDisplayWidth"(arg0: integer): void
 "getTextureId"(): integer
 "getRGB"(arg0: integer, arg1: integer): integer
 "getAlpha"(): float
 "setAlpha"(arg0: float): void
 "hasImage"(): boolean
}

export namespace $Texture {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Texture$Type = ($Texture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Texture_ = $Texture$Type;
}}
declare module "packages/journeymap/client/api/display/$IThemeButton$Action" {
import {$IThemeButton, $IThemeButton$Type} from "packages/journeymap/client/api/display/$IThemeButton"

export interface $IThemeButton$Action {

 "doAction"(arg0: $IThemeButton$Type): void

(arg0: $IThemeButton$Type): void
}

export namespace $IThemeButton$Action {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IThemeButton$Action$Type = ($IThemeButton$Action);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IThemeButton$Action_ = $IThemeButton$Action$Type;
}}
declare module "packages/journeymap/client/api/display/$ModPopupMenu" {
import {$ModPopupMenu$Action, $ModPopupMenu$Action$Type} from "packages/journeymap/client/api/display/$ModPopupMenu$Action"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ModPopupMenu {

 "createSubItemList"(arg0: string): $ModPopupMenu
 "addMenuItem"(arg0: string, arg1: $ModPopupMenu$Action$Type): $ModPopupMenu
 "addMenuItemScreen"(arg0: string, arg1: $Screen$Type): $ModPopupMenu
}

export namespace $ModPopupMenu {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModPopupMenu$Type = ($ModPopupMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModPopupMenu_ = $ModPopupMenu$Type;
}}
declare module "packages/journeymap/client/ui/option/$SlotMetadata" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$SlotMetadata$ValueType, $SlotMetadata$ValueType$Type} from "packages/journeymap/client/ui/option/$SlotMetadata$ValueType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"

export class $SlotMetadata<T> implements $Comparable<($SlotMetadata)> {

constructor(arg0: $Button$Type, arg1: string, arg2: string)
constructor(arg0: $Button$Type, arg1: string, arg2: string, arg3: integer)
constructor(arg0: $Button$Type, arg1: string, arg2: string, arg3: string, arg4: T, arg5: boolean)
constructor(arg0: $Button$Type)
constructor(arg0: $Button$Type, arg1: integer)
constructor(arg0: $Button$Type, arg1: boolean)
constructor(arg0: $Button$Type, arg1: string, arg2: string, arg3: boolean)

public "getName"(): string
public "compareTo"(arg0: $SlotMetadata$Type<(any)>): integer
public "getProperties"(): $PropertiesBase
public "getDefaultValue"(): T
public "isToolbar"(): boolean
public "hasConfigField"(): boolean
public "getRange"(): string
public "setMasterPropertyForCategory"(arg0: boolean): void
public "setOrder"(arg0: integer): void
public "getOrder"(): integer
public "getTooltipLines"(): $List<($FormattedCharSequence)>
public "getValueList"(): $List<(any)>
public "getButton"(): $Button
public "isMaster"(): boolean
public "getTooltip"(): $List<($FormattedCharSequence)>
public "isAdvanced"(): boolean
public "resetToDefaultValue"(): void
public "getValueType"(): $SlotMetadata$ValueType
public "setValueList"(arg0: $List$Type<(any)>): void
public "isMasterPropertyForCategory"(): boolean
public "setAdvanced"(arg0: boolean): void
public "updateFromButton"(): void
get "name"(): string
get "properties"(): $PropertiesBase
get "defaultValue"(): T
get "toolbar"(): boolean
get "range"(): string
set "masterPropertyForCategory"(value: boolean)
set "order"(value: integer)
get "order"(): integer
get "tooltipLines"(): $List<($FormattedCharSequence)>
get "valueList"(): $List<(any)>
get "button"(): $Button
get "master"(): boolean
get "tooltip"(): $List<($FormattedCharSequence)>
get "advanced"(): boolean
get "valueType"(): $SlotMetadata$ValueType
set "valueList"(value: $List$Type<(any)>)
get "masterPropertyForCategory"(): boolean
set "advanced"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotMetadata$Type<T> = ($SlotMetadata<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotMetadata_<T> = $SlotMetadata$Type<(T)>;
}}
declare module "packages/journeymap/client/render/draw/$DrawPolygonStep" {
import {$DrawStep$Pass, $DrawStep$Pass$Type} from "packages/journeymap/client/render/draw/$DrawStep$Pass"
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$MapPolygon, $MapPolygon$Type} from "packages/journeymap/client/api/model/$MapPolygon"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PolygonOverlay, $PolygonOverlay$Type} from "packages/journeymap/client/api/display/$PolygonOverlay"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BaseOverlayDrawStep, $BaseOverlayDrawStep$Type} from "packages/journeymap/client/render/draw/$BaseOverlayDrawStep"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"

export class $DrawPolygonStep extends $BaseOverlayDrawStep<($PolygonOverlay)> {
readonly "overlay": T

constructor(arg0: $PolygonOverlay$Type)

public static "triangulate"(arg0: $PolygonOverlay$Type): $List<($MapPolygon)>
public "draw"(arg0: $GuiGraphics$Type, arg1: $MultiBufferSource$Type, arg2: $DrawStep$Pass$Type, arg3: double, arg4: double, arg5: $GridRenderer$Type, arg6: double, arg7: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawPolygonStep$Type = ($DrawPolygonStep);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawPolygonStep_ = $DrawPolygonStep$Type;
}}
declare module "packages/journeymap/client/api/event/$DisplayUpdateEvent" {
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $DisplayUpdateEvent extends $ClientEvent {
readonly "uiState": $UIState
readonly "type": $ClientEvent$Type
readonly "dimension": $ResourceKey<($Level)>
readonly "timestamp": long

constructor(arg0: $UIState$Type)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayUpdateEvent$Type = ($DisplayUpdateEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayUpdateEvent_ = $DisplayUpdateEvent$Type;
}}
declare module "packages/journeymap/client/ui/option/$SlotMetadata$ValueType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SlotMetadata$ValueType extends $Enum<($SlotMetadata$ValueType)> {
static readonly "Boolean": $SlotMetadata$ValueType
static readonly "Set": $SlotMetadata$ValueType
static readonly "Integer": $SlotMetadata$ValueType
static readonly "Toolbar": $SlotMetadata$ValueType


public static "values"(): ($SlotMetadata$ValueType)[]
public static "valueOf"(arg0: string): $SlotMetadata$ValueType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotMetadata$ValueType$Type = (("toolbar") | ("boolean") | ("set") | ("integer")) | ($SlotMetadata$ValueType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotMetadata$ValueType_ = $SlotMetadata$ValueType$Type;
}}
declare module "packages/journeymap/client/api/event/$RegistryEvent$RegistryType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RegistryEvent$RegistryType extends $Enum<($RegistryEvent$RegistryType)> {
static readonly "OPTIONS": $RegistryEvent$RegistryType
static readonly "INFO_SLOT": $RegistryEvent$RegistryType


public static "values"(): ($RegistryEvent$RegistryType)[]
public static "valueOf"(arg0: string): $RegistryEvent$RegistryType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvent$RegistryType$Type = (("info_slot") | ("options")) | ($RegistryEvent$RegistryType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvent$RegistryType_ = $RegistryEvent$RegistryType$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$SortButton" {
import {$WaypointManagerItem$Sort, $WaypointManagerItem$Sort$Type} from "packages/journeymap/client/ui/waypoint/$WaypointManagerItem$Sort"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$OnOffButton, $OnOffButton$Type} from "packages/journeymap/client/ui/component/$OnOffButton"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $SortButton extends $OnOffButton {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: string, arg1: $WaypointManagerItem$Sort$Type, arg2: $Button$OnPress$Type)

public "setActive"(arg0: boolean): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "toggle"(): void
set "active"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortButton$Type = ($SortButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortButton_ = $SortButton$Type;
}}
declare module "packages/journeymap/client/api/$IClientAPI" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$DisplayType, $DisplayType$Type} from "packages/journeymap/client/api/display/$DisplayType"
import {$EnumSet, $EnumSet$Type} from "packages/java/util/$EnumSet"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Waypoint, $Waypoint$Type} from "packages/journeymap/client/api/display/$Waypoint"
import {$Context$MapType, $Context$MapType$Type} from "packages/journeymap/client/api/display/$Context$MapType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Displayable, $Displayable$Type} from "packages/journeymap/client/api/display/$Displayable"
import {$Context$UI, $Context$UI$Type} from "packages/journeymap/client/api/display/$Context$UI"
import {$ClientEvent$Type, $ClientEvent$Type$Type} from "packages/journeymap/client/api/event/$ClientEvent$Type"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export interface $IClientAPI {

 "remove"(arg0: $Displayable$Type): void
 "removeAll"(arg0: string, arg1: $DisplayType$Type): void
 "removeAll"(arg0: string): void
 "exists"(arg0: $Displayable$Type): boolean
 "getDataPath"(arg0: string): $File
/**
 * 
 * @deprecated
 */
 "setWorldId"(arg0: string): void
 "isDisplayEnabled"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type): boolean
 "isWaypointsEnabled"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type): boolean
 "getWaypoint"(arg0: string, arg1: string): $Waypoint
 "getAllWaypoints"(arg0: $ResourceKey$Type<($Level$Type)>): $List<($Waypoint)>
 "getAllWaypoints"(): $List<($Waypoint)>
 "toggleWaypoints"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type, arg3: boolean): void
 "getWaypoints"(arg0: string): $List<($Waypoint)>
 "playerAccepts"(arg0: string, arg1: $DisplayType$Type): boolean
 "getUIState"(arg0: $Context$UI$Type): $UIState
 "requestMapTile"(arg0: string, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Context$MapType$Type, arg3: $ChunkPos$Type, arg4: $ChunkPos$Type, arg5: integer, arg6: integer, arg7: boolean, arg8: $Consumer$Type<($NativeImage$Type)>): void
 "toggleDisplay"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $Context$MapType$Type, arg2: $Context$UI$Type, arg3: boolean): void
 "subscribe"(arg0: string, arg1: $EnumSet$Type<($ClientEvent$Type$Type)>): void
 "getWorldId"(): string
 "show"(arg0: $Displayable$Type): void
}

export namespace $IClientAPI {
const API_OWNER: string
const API_VERSION: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientAPI$Type = ($IClientAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientAPI_ = $IClientAPI$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeChunkEvents" {
import {$ChunkWatchEvent, $ChunkWatchEvent$Type} from "packages/net/minecraftforge/event/level/$ChunkWatchEvent"
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$BlockEvent, $BlockEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent"
import {$ChunkEvent$Load, $ChunkEvent$Load$Type} from "packages/net/minecraftforge/event/level/$ChunkEvent$Load"

export class $ForgeChunkEvents implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onChunkUpdate"(arg0: $ChunkWatchEvent$Type): void
public "onBlockUpdate"(arg0: $BlockEvent$Type): void
public "onChunkLoad"(arg0: $ChunkEvent$Load$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeChunkEvents$Type = ($ForgeChunkEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeChunkEvents_ = $ForgeChunkEvents$Type;
}}
declare module "packages/journeymap/client/cartography/render/$BiomeRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$SurfaceRenderer, $SurfaceRenderer$Type} from "packages/journeymap/client/cartography/render/$SurfaceRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $BiomeRenderer extends $SurfaceRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor()

public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeRenderer$Type = ($BiomeRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeRenderer_ = $BiomeRenderer$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent$AddonButtonDisplayEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ThemeButtonDisplay, $ThemeButtonDisplay$Type} from "packages/journeymap/client/api/display/$ThemeButtonDisplay"
import {$FullscreenDisplayEvent, $FullscreenDisplayEvent$Type} from "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent"

export class $FullscreenDisplayEvent$AddonButtonDisplayEvent extends $FullscreenDisplayEvent {

constructor(arg0: $IFullscreen$Type, arg1: $ThemeButtonDisplay$Type)
constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenDisplayEvent$AddonButtonDisplayEvent$Type = ($FullscreenDisplayEvent$AddonButtonDisplayEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenDisplayEvent$AddonButtonDisplayEvent_ = $FullscreenDisplayEvent$AddonButtonDisplayEvent$Type;
}}
declare module "packages/journeymap/client/ui/theme/impl/$Style" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Style {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Style$Type = ($Style);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Style_ = $Style$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$ReticleOrientation" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $ReticleOrientation extends $Enum<($ReticleOrientation)> implements $KeyedEnum {
static readonly "Compass": $ReticleOrientation
static readonly "PlayerHeading": $ReticleOrientation
readonly "key": string


public "toString"(): string
public static "values"(): ($ReticleOrientation)[]
public static "valueOf"(arg0: string): $ReticleOrientation
public "getKey"(): string
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReticleOrientation$Type = (("playerheading") | ("compass")) | ($ReticleOrientation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReticleOrientation_ = $ReticleOrientation$Type;
}}
declare module "packages/journeymap/client/api/display/$ModPopupMenu$Action" {
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $ModPopupMenu$Action {

 "doAction"(arg0: $BlockPos$Type): void

(arg0: $BlockPos$Type): void
}

export namespace $ModPopupMenu$Action {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModPopupMenu$Action$Type = ($ModPopupMenu$Action);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModPopupMenu$Action_ = $ModPopupMenu$Action$Type;
}}
declare module "packages/journeymap/client/api/model/$IBlockInfo" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $IBlockInfo {

 "getRegionZ"(): integer
 "getRegionX"(): integer
 "getBlock"(): $Block
 "getBiome"(): $Biome
 "getChunkPos"(): $ChunkPos
 "getBlockPos"(): $BlockPos
 "getBlockState"(): $BlockState
 "getChunk"(): $LevelChunk
}

export namespace $IBlockInfo {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockInfo$Type = ($IBlockInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockInfo_ = $IBlockInfo$Type;
}}
declare module "packages/journeymap/client/mod/vanilla/$VanillaBlockSpriteProxy" {
import {$IBlockSpritesProxy, $IBlockSpritesProxy$Type} from "packages/journeymap/client/mod/$IBlockSpritesProxy"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$FluidAccess, $FluidAccess$Type} from "packages/journeymap/common/accessors/$FluidAccess"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$FlowingFluid, $FlowingFluid$Type} from "packages/net/minecraft/world/level/material/$FlowingFluid"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ColoredSprite, $ColoredSprite$Type} from "packages/journeymap/client/cartography/color/$ColoredSprite"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $VanillaBlockSpriteProxy implements $IBlockSpritesProxy, $FluidAccess {

constructor()

public "addSprites"(arg0: $HashMap$Type<(string), ($ColoredSprite$Type)>, arg1: $List$Type<($BakedQuad$Type)>): boolean
public "getSprites"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): $Collection<($ColoredSprite)>
public "getFluid"(arg0: $Block$Type): $FlowingFluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaBlockSpriteProxy$Type = ($VanillaBlockSpriteProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaBlockSpriteProxy_ = $VanillaBlockSpriteProxy$Type;
}}
declare module "packages/journeymap/client/render/ingame/$WaypointDecorationRenderer" {
import {$WaypointRenderer, $WaypointRenderer$Type} from "packages/journeymap/client/render/ingame/$WaypointRenderer"

export class $WaypointDecorationRenderer extends $WaypointRenderer {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointDecorationRenderer$Type = ($WaypointDecorationRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointDecorationRenderer_ = $WaypointDecorationRenderer$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/event/$FullscreenEventReceiver" {
import {$WaypointEvent, $WaypointEvent$Type} from "packages/journeymap/client/api/event/$WaypointEvent"
import {$IClientAPI, $IClientAPI$Type} from "packages/journeymap/client/api/$IClientAPI"
import {$IClientPlugin, $IClientPlugin$Type} from "packages/journeymap/client/api/$IClientPlugin"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"
import {$EntityRadarUpdateEvent, $EntityRadarUpdateEvent$Type} from "packages/journeymap/client/api/event/forge/$EntityRadarUpdateEvent"

export class $FullscreenEventReceiver implements $IClientPlugin {

constructor()

public "initialize"(arg0: $IClientAPI$Type): void
public "onEvent"(arg0: $ClientEvent$Type): void
public "onRadarEntityUpdateEvent"(arg0: $EntityRadarUpdateEvent$Type): void
public "getModId"(): string
public "onWaypointEvent"(arg0: $WaypointEvent$Type): void
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenEventReceiver$Type = ($FullscreenEventReceiver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenEventReceiver_ = $FullscreenEventReceiver$Type;
}}
declare module "packages/journeymap/client/waypoint/$Waypoint$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Waypoint$Type extends $Enum<($Waypoint$Type)> {
static readonly "Normal": $Waypoint$Type
static readonly "Death": $Waypoint$Type


public static "values"(): ($Waypoint$Type)[]
public static "valueOf"(arg0: string): $Waypoint$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Waypoint$Type$Type = (("normal") | ("death")) | ($Waypoint$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Waypoint$Type_ = $Waypoint$Type$Type;
}}
declare module "packages/journeymap/client/data/$AllData$Key" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $AllData$Key extends $Enum<($AllData$Key)> {
static readonly "animals": $AllData$Key
static readonly "images": $AllData$Key
static readonly "mobs": $AllData$Key
static readonly "player": $AllData$Key
static readonly "players": $AllData$Key
static readonly "villagers": $AllData$Key
static readonly "waypoints": $AllData$Key
static readonly "world": $AllData$Key


public static "values"(): ($AllData$Key)[]
public static "valueOf"(arg0: string): $AllData$Key
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AllData$Key$Type = (("mobs") | ("images") | ("world") | ("players") | ("villagers") | ("animals") | ("waypoints") | ("player")) | ($AllData$Key);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AllData$Key_ = $AllData$Key$Type;
}}
declare module "packages/journeymap/client/properties/$AddonProperties" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$ClientPropertiesBase, $ClientPropertiesBase$Type} from "packages/journeymap/client/properties/$ClientPropertiesBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AddonProperties extends $ClientPropertiesBase {

constructor()

public "getName"(): string
public "setName"(arg0: string): void
public "getFile"(): $File
public "getFileName"(): string
public "getConfigFields"(): $Map<(string), ($ConfigField<(any)>)>
public "setFieldMap"(arg0: $Map$Type<(string), ($ConfigField$Type<(any)>)>): $AddonProperties
public "getParentCategory"(): $Category
get "name"(): string
set "name"(value: string)
get "file"(): $File
get "fileName"(): string
get "configFields"(): $Map<(string), ($ConfigField<(any)>)>
set "fieldMap"(value: $Map$Type<(string), ($ConfigField$Type<(any)>)>)
get "parentCategory"(): $Category
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddonProperties$Type = ($AddonProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddonProperties_ = $AddonProperties$Type;
}}
declare module "packages/journeymap/client/ui/waypoint/$DimensionsDropDownButton" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$DropDownItem, $DropDownItem$Type} from "packages/journeymap/client/ui/component/$DropDownItem"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DropDownButton, $DropDownButton$Type} from "packages/journeymap/client/ui/component/$DropDownButton"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $DimensionsDropDownButton extends $DropDownButton {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Button$OnPress$Type)

public "setDim"(arg0: $ResourceKey$Type<($Level$Type)>): void
public "setSelected"(arg0: $DropDownItem$Type): void
set "dim"(value: $ResourceKey$Type<($Level$Type)>)
set "selected"(value: $DropDownItem$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionsDropDownButton$Type = ($DimensionsDropDownButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionsDropDownButton_ = $DimensionsDropDownButton$Type;
}}
declare module "packages/journeymap/client/ui/fullscreen/layer/$WaypointLayer" {
import {$Fullscreen, $Fullscreen$Type} from "packages/journeymap/client/ui/fullscreen/$Fullscreen"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$Layer, $Layer$Type} from "packages/journeymap/client/ui/fullscreen/layer/$Layer"

export class $WaypointLayer extends $Layer {

constructor(arg0: $Fullscreen$Type)

public "propagateClick"(): boolean
public "onMouseClick"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: integer, arg5: boolean, arg6: float): $List<($DrawStep)>
public "onMouseMove"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $Point2D$Double$Type, arg3: $BlockPos$Type, arg4: float, arg5: boolean): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointLayer$Type = ($WaypointLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointLayer_ = $WaypointLayer$Type;
}}
declare module "packages/journeymap/client/ui/component/$OnOffButton$ToggleListener" {
import {$OnOffButton, $OnOffButton$Type} from "packages/journeymap/client/ui/component/$OnOffButton"

export interface $OnOffButton$ToggleListener {

 "onToggle"(arg0: $OnOffButton$Type, arg1: boolean): boolean

(arg0: $OnOffButton$Type, arg1: boolean): boolean
}

export namespace $OnOffButton$ToggleListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OnOffButton$ToggleListener$Type = ($OnOffButton$ToggleListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OnOffButton$ToggleListener_ = $OnOffButton$ToggleListener$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeKeyEvents" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$ScreenEvent$KeyPressed$Post, $ScreenEvent$KeyPressed$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$KeyPressed$Post"
import {$KeyEvent, $KeyEvent$Type} from "packages/journeymap/client/event/handlers/keymapping/$KeyEvent"
import {$ScreenEvent$MouseButtonPressed$Post, $ScreenEvent$MouseButtonPressed$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed$Post"
import {$InputEvent$MouseButton$Post, $InputEvent$MouseButton$Post$Type} from "packages/net/minecraftforge/client/event/$InputEvent$MouseButton$Post"
import {$KeyEventHandler, $KeyEventHandler$Type} from "packages/journeymap/client/event/handlers/$KeyEventHandler"
import {$RegisterKeyMappingsEvent, $RegisterKeyMappingsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterKeyMappingsEvent"

export class $ForgeKeyEvents implements $KeyEvent, $ForgeEventHandlerManager$EventHandler {

constructor()

public "register"(arg0: $KeyMapping$Type): $KeyMapping
public "getHandler"(): $KeyEventHandler
public static "onKeyRegisterEvent"(arg0: $RegisterKeyMappingsEvent$Type): void
public "onGuiMouseEvent"(arg0: $ScreenEvent$MouseButtonPressed$Post$Type): void
public "onMouseEvent"(arg0: $InputEvent$MouseButton$Post$Type): void
public "onGuiKeyboardEvent"(arg0: $ScreenEvent$KeyPressed$Post$Type): void
public "onGameKeyboardEvent"(arg0: $InputEvent$Key$Type): void
get "handler"(): $KeyEventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeKeyEvents$Type = ($ForgeKeyEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeKeyEvents_ = $ForgeKeyEvents$Type;
}}
declare module "packages/journeymap/client/$Constants" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$PackRepository, $PackRepository$Type} from "packages/net/minecraft/server/packs/repository/$PackRepository"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Ordering, $Ordering$Type} from "packages/com/google/common/collect/$Ordering"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$TimeZone, $TimeZone$Type} from "packages/java/util/$TimeZone"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $Constants {
static readonly "CASE_INSENSITIVE_NULL_SAFE_ORDER": $Ordering<(string)>
static readonly "GMT": $TimeZone
static "JOURNEYMAP_DIR": string
static "CONFIG_DIR_LEGACY": string
static "CONFIG_DIR": string
static "DATA_DIR": string
static "SP_DATA_DIR": string
static "MP_DATA_DIR": string
static "RESOURCE_PACKS_DEFAULT": string
static "WEB_DIR": string
static "ENTITY_ICON_DIR": string
static "WAYPOINT_ICON_DIR": string
static "THEME_ICON_DIR": string

constructor()

public static "getString"(arg0: string, ...arg1: (any)[]): string
public static "getString"(arg0: string): string
public static "getLocale"(): $Locale
public static "getResourcePacks"(): $PackRepository
public static "birthdayMessage"(): string
public static "getStringTextComponent"(arg0: string): $MutableComponent
public static "safeEqual"(arg0: string, arg1: string): boolean
public static "getFormattedText"(arg0: string, arg1: $Style$Type, arg2: $Font$Type, arg3: integer): $FormattedCharSequence
public static "getTranslatedTextComponent"(arg0: string): $MutableComponent
get "locale"(): $Locale
get "resourcePacks"(): $PackRepository
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
declare module "packages/journeymap/client/ui/theme/$Theme" {
import {$Theme$ImageSpec, $Theme$ImageSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ImageSpec"
import {$Theme$Fullscreen, $Theme$Fullscreen$Type} from "packages/journeymap/client/ui/theme/$Theme$Fullscreen"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Theme$Control, $Theme$Control$Type} from "packages/journeymap/client/ui/theme/$Theme$Control"
import {$Theme$Container, $Theme$Container$Type} from "packages/journeymap/client/ui/theme/$Theme$Container"
import {$Theme$Minimap, $Theme$Minimap$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap"

export class $Theme {
static readonly "VERSION": double
 "schema": integer
 "author": string
 "name": string
 "directory": string
 "container": $Theme$Container
 "control": $Theme$Control
 "fullscreen": $Theme$Fullscreen
 "icon": $Theme$ImageSpec
 "minimap": $Theme$Minimap

constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "toHexColor"(arg0: integer): string
public static "toHexColor"(arg0: $Color$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Type = ($Theme);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme_ = $Theme$Type;
}}
declare module "packages/journeymap/client/cartography/$IChunkRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export interface $IChunkRenderer {

 "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
 "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
 "getAmbientColor"(): (float)[]
}

export namespace $IChunkRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IChunkRenderer$Type = ($IChunkRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IChunkRenderer_ = $IChunkRenderer$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeMinimapFrame" {
import {$Texture, $Texture$Type} from "packages/journeymap/client/texture/$Texture"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$MiniMapProperties, $MiniMapProperties$Type} from "packages/journeymap/client/properties/$MiniMapProperties"
import {$ReticleOrientation, $ReticleOrientation$Type} from "packages/journeymap/client/ui/minimap/$ReticleOrientation"
import {$Theme$Minimap$MinimapSpec, $Theme$Minimap$MinimapSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$Minimap$MinimapSpec"
import {$Rectangle2D$Double, $Rectangle2D$Double$Type} from "packages/java/awt/geom/$Rectangle2D$Double"

export class $ThemeMinimapFrame {

constructor(arg0: $Theme$Type, arg1: $Theme$Minimap$MinimapSpec$Type, arg2: $MiniMapProperties$Type, arg3: integer, arg4: integer)

public "drawFrame"(arg0: $PoseStack$Type): void
public "drawReticle"(arg0: $PoseStack$Type): void
public "drawMask"(arg0: $PoseStack$Type): void
public "getY"(): double
public "setX"(arg0: double): void
public "setY"(arg0: double): void
public "getX"(): double
public "getWidth"(): double
public "getHeight"(): double
public "setPosition"(arg0: double, arg1: double): void
public "getReticleOrientation"(): $ReticleOrientation
public "getCompassPoint"(): $Texture
public "getFrameBounds"(): $Rectangle2D$Double
get "y"(): double
set "x"(value: double)
set "y"(value: double)
get "x"(): double
get "width"(): double
get "height"(): double
get "reticleOrientation"(): $ReticleOrientation
get "compassPoint"(): $Texture
get "frameBounds"(): $Rectangle2D$Double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeMinimapFrame$Type = ($ThemeMinimapFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeMinimapFrame_ = $ThemeMinimapFrame$Type;
}}
declare module "packages/journeymap/client/log/$JMLogger" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $JMLogger {
static readonly "DEPRECATED_LOG_FILE": string
static readonly "LOG_FILE": string

constructor()

public static "init"(): $Logger
public static "getLogFile"(): $File
public static "getPropertiesSummary"(): string
public static "logProperties"(): void
public static "logOnce"(arg0: string): void
public static "throwLogOnce"(arg0: string, arg1: $Throwable$Type): void
public static "setLevelFromProperties"(): void
get "logFile"(): $File
get "propertiesSummary"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMLogger$Type = ($JMLogger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMLogger_ = $JMLogger$Type;
}}
declare module "packages/journeymap/client/command/$CmdChatPosition" {
import {$JMCommand, $JMCommand$Type} from "packages/journeymap/client/command/$JMCommand"
import {$CommandSource, $CommandSource$Type} from "packages/net/minecraft/commands/$CommandSource"

export class $CmdChatPosition implements $JMCommand {

constructor()

public "getName"(): string
public "execute"(arg0: $CommandSource$Type, arg1: (string)[]): integer
public "getUsage"(arg0: $CommandSource$Type): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdChatPosition$Type = ($CmdChatPosition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdChatPosition_ = $CmdChatPosition$Type;
}}
declare module "packages/journeymap/client/task/multi/$MapRegionTask" {
import {$BaseMapTask, $BaseMapTask$Type} from "packages/journeymap/client/task/multi/$BaseMapTask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$File, $File$Type} from "packages/java/io/$File"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"
import {$ChunkRenderController, $ChunkRenderController$Type} from "packages/journeymap/client/cartography/$ChunkRenderController"

export class $MapRegionTask extends $BaseMapTask {
static "MAP_TYPE": $MapType
static "active": boolean


public static "create"(arg0: $ChunkRenderController$Type, arg1: $RegionCoord$Type, arg2: $MapType$Type, arg3: $Minecraft$Type): $BaseMapTask
public "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "getMaxRuntime"(): integer
public static "getMemoryUsage"(): long
get "maxRuntime"(): integer
get "memoryUsage"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapRegionTask$Type = ($MapRegionTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapRegionTask_ = $MapRegionTask$Type;
}}
declare module "packages/journeymap/client/api/model/$WaypointBase" {
import {$IWaypointDisplay, $IWaypointDisplay$Type} from "packages/journeymap/client/api/display/$IWaypointDisplay"
import {$Displayable, $Displayable$Type} from "packages/journeymap/client/api/display/$Displayable"
import {$MapImage, $MapImage$Type} from "packages/journeymap/client/api/model/$MapImage"

export class $WaypointBase<T extends $WaypointBase<(any)>> extends $Displayable implements $IWaypointDisplay {


public "getName"(): string
public "equals"(arg0: any): boolean
public "setName"(arg0: string): T
public "isDirty"(): boolean
public "setColor"(arg0: integer): T
public "clearIcon"(): T
public "setDirty"(): T
public "setDirty"(arg0: boolean): T
public "getBackgroundColor"(): integer
public "clearColor"(): T
public "clearBackgroundColor"(): T
public "getDisplayDimensions"(): (string)[]
public "setDisplayDimensions"(...arg0: (string)[]): T
public "hasDisplayDimensions"(): boolean
public "clearDisplayDimensions"(): T
public "getIcon"(): $MapImage
public "setBackgroundColor"(arg0: integer): T
public "hasBackgroundColor"(): boolean
public "getColor"(): integer
public "setIcon"(arg0: $MapImage$Type): T
public "hasIcon"(): boolean
public "isDisplayed"(arg0: string): boolean
public "setDisplayed"(arg0: string, arg1: boolean): void
public "hasColor"(): boolean
get "name"(): string
set "name"(value: string)
get "dirty"(): boolean
set "color"(value: integer)
set "dirty"(value: boolean)
get "backgroundColor"(): integer
get "displayDimensions"(): (string)[]
set "displayDimensions"(value: (string)[])
get "icon"(): $MapImage
set "backgroundColor"(value: integer)
get "color"(): integer
set "icon"(value: $MapImage$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointBase$Type<T> = ($WaypointBase<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointBase_<T> = $WaypointBase$Type<(T)>;
}}
declare module "packages/journeymap/client/ui/component/$BooleanPropertyButton" {
import {$IConfigFieldHolder, $IConfigFieldHolder$Type} from "packages/journeymap/client/ui/component/$IConfigFieldHolder"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OnOffButton, $OnOffButton$Type} from "packages/journeymap/client/ui/component/$OnOffButton"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $BooleanPropertyButton extends $OnOffButton implements $IConfigFieldHolder<($BooleanField)> {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: string, arg1: string, arg2: $BooleanField$Type, arg3: $Button$OnPress$Type)

public "getField"(): $BooleanField
public "setValue"(arg0: boolean): void
public "refresh"(): void
public "getConfigField"(): $BooleanField
public "toggle"(): void
get "field"(): $BooleanField
set "value"(value: boolean)
get "configField"(): $BooleanField
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanPropertyButton$Type = ($BooleanPropertyButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanPropertyButton_ = $BooleanPropertyButton$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$MiniMap" {
import {$Shape, $Shape$Type} from "packages/journeymap/client/ui/minimap/$Shape"
import {$Selectable, $Selectable$Type} from "packages/journeymap/client/ui/minimap/$Selectable"
import {$Position, $Position$Type} from "packages/journeymap/client/ui/minimap/$Position"
import {$MapState, $MapState$Type} from "packages/journeymap/client/model/$MapState"
import {$MiniMapProperties, $MiniMapProperties$Type} from "packages/journeymap/client/properties/$MiniMapProperties"
import {$DisplayVars, $DisplayVars$Type} from "packages/journeymap/client/ui/minimap/$DisplayVars"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$UIState, $UIState$Type} from "packages/journeymap/client/api/util/$UIState"

export class $MiniMap implements $Selectable {

constructor(arg0: $MiniMapProperties$Type)

public "getLocation"(): string
public "reset"(): void
public "resetState"(): void
public static "state"(): $MapState
public "tick"(): void
public "withinBounds"(arg0: double, arg1: double): boolean
public "resetInitTime"(): void
public "updateDisplayVars"(arg0: $Shape$Type, arg1: float, arg2: float, arg3: $Position$Type, arg4: boolean, arg5: boolean): void
public "updateDisplayVars"(arg0: boolean, arg1: boolean): void
public "updateDisplayVars"(arg0: boolean): void
public "getDisplayVars"(): $DisplayVars
public "isDrawing"(): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "getBiome"(): string
public static "uiState"(): $UIState
public "drawMap"(arg0: $GuiGraphics$Type): void
public "drawMap"(arg0: $GuiGraphics$Type, arg1: boolean): void
public "renderBorder"(arg0: $GuiGraphics$Type, arg1: integer): void
public "getCurrentMinimapProperties"(): $MiniMapProperties
public "setMiniMapProperties"(arg0: $MiniMapProperties$Type): void
public "validateScreenBounds"(arg0: double, arg1: double): $Vec2
public static "updateUIState"(arg0: boolean): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
get "location"(): string
get "displayVars"(): $DisplayVars
get "drawing"(): boolean
get "biome"(): string
get "currentMinimapProperties"(): $MiniMapProperties
set "miniMapProperties"(value: $MiniMapProperties$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MiniMap$Type = ($MiniMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MiniMap_ = $MiniMap$Type;
}}
declare module "packages/journeymap/client/ui/component/$ListPropertyButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$IConfigFieldHolder, $IConfigFieldHolder$Type} from "packages/journeymap/client/ui/component/$IConfigFieldHolder"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $ListPropertyButton<T> extends $Button implements $IConfigFieldHolder<($ConfigField<(T)>)> {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Collection$Type<(T)>, arg1: string, arg2: $ConfigField$Type<(T)>, arg3: $Button$OnPress$Type)
constructor(arg0: $Collection$Type<(T)>, arg1: string, arg2: $ConfigField$Type<(T)>)

public "getField"(): $ConfigField<(T)>
public "setValue"(arg0: T): void
public "refresh"(): void
public "getConfigField"(): $ConfigField<(T)>
public "nextOption"(): void
public "prevOption"(): void
public "getFitWidth"(arg0: $Font$Type): integer
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "keyTyped"(arg0: character, arg1: integer): boolean
get "field"(): $ConfigField<(T)>
set "value"(value: T)
get "configField"(): $ConfigField<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListPropertyButton$Type<T> = ($ListPropertyButton<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListPropertyButton_<T> = $ListPropertyButton$Type<(T)>;
}}
declare module "packages/journeymap/client/api/impl/$OptionsDisplayFactory" {
import {$AddonProperties, $AddonProperties$Type} from "packages/journeymap/client/properties/$AddonProperties"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $OptionsDisplayFactory {
static readonly "PROPERTIES_REGISTRY": $Map<(string), ($AddonProperties)>
static readonly "MOD_FIELD_REGISTRY": $Map<(string), ($Map<(string), ($ConfigField<(any)>)>)>

constructor()

public "load"(): $OptionsDisplayFactory
public static "register"(arg0: string, arg1: $Map$Type<(string), ($ConfigField$Type<(any)>)>): void
public static "register"(arg0: string, arg1: $AddonProperties$Type): void
public "save"(): void
public "buildAddonProperties"(): $OptionsDisplayFactory
public static "getAllFields"(): $Map<(string), ($ConfigField<(any)>)>
get "allFields"(): $Map<(string), ($ConfigField<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsDisplayFactory$Type = ($OptionsDisplayFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsDisplayFactory_ = $OptionsDisplayFactory$Type;
}}
declare module "packages/journeymap/client/io/nbt/$CustomChunkReader" {
import {$CustomChunkReader$ProcessedChunk, $CustomChunkReader$ProcessedChunk$Type} from "packages/journeymap/client/io/nbt/$CustomChunkReader$ProcessedChunk"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$PoiManager, $PoiManager$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiManager"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $CustomChunkReader {

constructor()

public static "read"(arg0: $ServerLevel$Type, arg1: $PoiManager$Type, arg2: $ChunkPos$Type, arg3: $CompoundTag$Type): $CustomChunkReader$ProcessedChunk
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomChunkReader$Type = ($CustomChunkReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomChunkReader_ = $CustomChunkReader$Type;
}}
declare module "packages/journeymap/client/mod/vanilla/$FlowerBlockProxy" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FlowerBlockProxy extends $Enum<($FlowerBlockProxy)> implements $IBlockColorProxy {
static readonly "INSTANCE": $FlowerBlockProxy


public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public static "values"(): ($FlowerBlockProxy)[]
public static "valueOf"(arg0: string): $FlowerBlockProxy
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowerBlockProxy$Type = (("instance")) | ($FlowerBlockProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowerBlockProxy_ = $FlowerBlockProxy$Type;
}}
declare module "packages/journeymap/client/ui/option/$LocationFormat$LocationFormatKeys" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LocationFormat$LocationFormatKeys {


public "format"(arg0: boolean, arg1: integer, arg2: integer, arg3: integer, arg4: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocationFormat$LocationFormatKeys$Type = ($LocationFormat$LocationFormatKeys);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocationFormat$LocationFormatKeys_ = $LocationFormat$LocationFormatKeys$Type;
}}
declare module "packages/journeymap/client/ui/component/$TextBoxButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $TextBoxButton extends $Button {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: any, arg1: $Font$Type, arg2: integer, arg3: integer, arg4: boolean, arg5: boolean)
constructor(arg0: any, arg1: $Font$Type, arg2: integer, arg3: integer)
constructor(arg0: string)

public "getText"(): string
public "setText"(arg0: string): void
public "getBottomY"(): integer
public "setVisible"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "isActive"(): boolean
public "setFocused"(arg0: boolean): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "isFocused"(): boolean
public "getMiddleY"(): integer
public "getCenterX"(): integer
public "getRightX"(): integer
public "getSelectedText"(): string
public "getHeight"(): integer
public "isHoveredOrFocused"(): boolean
public "setWidth"(arg0: integer): void
public "getWidth"(): integer
get "text"(): string
set "text"(value: string)
get "bottomY"(): integer
set "visible"(value: boolean)
get "active"(): boolean
set "focused"(value: boolean)
get "focused"(): boolean
get "middleY"(): integer
get "centerX"(): integer
get "rightX"(): integer
get "selectedText"(): string
get "height"(): integer
get "hoveredOrFocused"(): boolean
set "width"(value: integer)
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextBoxButton$Type = ($TextBoxButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextBoxButton_ = $TextBoxButton$Type;
}}
declare module "packages/journeymap/client/model/$EntityDTO" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$WrappedEntity, $WrappedEntity$Type} from "packages/journeymap/client/api/model/$WrappedEntity"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$WeakReference, $WeakReference$Type} from "packages/java/lang/ref/$WeakReference"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityDTO implements $WrappedEntity, $Serializable {
readonly "entityId": string
 "iconLocation": string
 "hostile": boolean
 "posX": double
 "posY": double
 "posZ": double
 "chunkCoordX": integer
 "chunkCoordY": integer
 "chunkCoordZ": integer
 "heading": double
 "serializedCustomName": string
 "owner": string
 "profession": string
 "username": string
 "biome": string
 "dimension": $ResourceKey<($Level)>
 "underground": boolean
 "invisible": boolean
 "sneaking": boolean
 "passiveAnimal": boolean
 "npc": boolean
 "color": integer
 "disabled": boolean


public "update"(arg0: $LivingEntity$Type, arg1: boolean): void
public "getOwner"(): $Entity
public "setColor"(arg0: integer): void
public "getPosition"(): $Vec3
public "getDimension"(): $ResourceKey<($Level)>
public "getProfession"(): string
public "getUnderground"(): boolean
public "getHeading"(): double
public "isInvisible"(): boolean
public "getHostile"(): boolean
public "setEntityToolTips"(arg0: $List$Type<($Component$Type)>): void
public "getBiome"(): $Biome
public "setDisable"(arg0: boolean): void
public "getPlayerName"(): string
public "getCustomName"(): $Component
public "isNpc"(): boolean
public "isSneaking"(): boolean
public "getEntityToolTips"(): $List<($Component)>
public "isPassiveAnimal"(): boolean
public "getEntityLivingRef"(): $WeakReference<($LivingEntity)>
public "getChunkPos"(): $BlockPos
public "getEntityIconLocation"(): $ResourceLocation
public "setEntityIconLocation"(arg0: $ResourceLocation$Type): void
public "getColor"(): integer
public "setCustomName"(arg0: $Component$Type): void
public "setCustomName"(arg0: string): void
public "isDisabled"(): boolean
public "getEntityId"(): string
get "owner"(): $Entity
set "color"(value: integer)
get "position"(): $Vec3
get "dimension"(): $ResourceKey<($Level)>
get "profession"(): string
get "underground"(): boolean
get "heading"(): double
get "invisible"(): boolean
get "hostile"(): boolean
set "entityToolTips"(value: $List$Type<($Component$Type)>)
get "biome"(): $Biome
set "disable"(value: boolean)
get "playerName"(): string
get "customName"(): $Component
get "npc"(): boolean
get "sneaking"(): boolean
get "entityToolTips"(): $List<($Component)>
get "passiveAnimal"(): boolean
get "entityLivingRef"(): $WeakReference<($LivingEntity)>
get "chunkPos"(): $BlockPos
get "entityIconLocation"(): $ResourceLocation
set "entityIconLocation"(value: $ResourceLocation$Type)
get "color"(): integer
set "customName"(value: $Component$Type)
set "customName"(value: string)
get "disabled"(): boolean
get "entityId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityDTO$Type = ($EntityDTO);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityDTO_ = $EntityDTO$Type;
}}
declare module "packages/journeymap/client/ui/minimap/$DisplayVars" {
import {$Shape, $Shape$Type} from "packages/journeymap/client/ui/minimap/$Shape"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Point2D$Double, $Point2D$Double$Type} from "packages/java/awt/geom/$Point2D$Double"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ThemeLabelSource$InfoSlot, $ThemeLabelSource$InfoSlot$Type} from "packages/journeymap/client/ui/theme/$ThemeLabelSource$InfoSlot"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$Theme$LabelSpec, $Theme$LabelSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$LabelSpec"

export class $DisplayVars {
readonly "minimapWidth": integer
readonly "minimapHeight": integer
readonly "textureX": integer
readonly "textureY": integer
readonly "centerPoint": $Point2D$Double
 "marginX": integer
 "marginY": integer


public "getShape"(): $Shape
public "drawInfoLabels"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: long): void
public "getInfoLabelAreaHeight"(arg0: $Font$Type, arg1: $Theme$LabelSpec$Type, ...arg2: ($ThemeLabelSource$InfoSlot$Type)[]): integer
get "shape"(): $Shape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayVars$Type = ($DisplayVars);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayVars_ = $DisplayVars$Type;
}}
declare module "packages/journeymap/client/mod/vanilla/$VanillaBlockColorProxy" {
import {$FluidAccess, $FluidAccess$Type} from "packages/journeymap/common/accessors/$FluidAccess"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$FlowingFluid, $FlowingFluid$Type} from "packages/net/minecraft/world/level/material/$FlowingFluid"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $VanillaBlockColorProxy implements $IBlockColorProxy, $FluidAccess {

constructor()

public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public "getColorMultiplier"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type, arg3: integer): integer
public static "getSpriteColor"(arg0: $BlockMD$Type, arg1: integer, arg2: $ChunkMD$Type, arg3: $BlockPos$Type): integer
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
public static "setBlockColorToError"(arg0: $BlockMD$Type): integer
public static "setBlockColorToMaterial"(arg0: $BlockMD$Type): integer
public "getFluid"(arg0: $Block$Type): $FlowingFluid
set "blockColorToError"(value: $BlockMD$Type)
set "blockColorToMaterial"(value: $BlockMD$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaBlockColorProxy$Type = ($VanillaBlockColorProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaBlockColorProxy_ = $VanillaBlockColorProxy$Type;
}}
declare module "packages/journeymap/client/event/handlers/keymapping/$KeyEvent" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"

export interface $KeyEvent {

 "register"(arg0: $KeyMapping$Type): $KeyMapping

(arg0: $KeyMapping$Type): $KeyMapping
}

export namespace $KeyEvent {
const probejs$$marker: never
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
declare module "packages/journeymap/client/properties/$WebMapProperties" {
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$CustomField, $CustomField$Type} from "packages/journeymap/common/properties/config/$CustomField"
import {$ClientPropertiesBase, $ClientPropertiesBase$Type} from "packages/journeymap/client/properties/$ClientPropertiesBase"

export class $WebMapProperties extends $ClientPropertiesBase {
readonly "enabled": $BooleanField
readonly "port": $CustomField

constructor()

public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WebMapProperties$Type = ($WebMapProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WebMapProperties_ = $WebMapProperties$Type;
}}
declare module "packages/journeymap/client/api/display/$PolygonOverlay" {
import {$Overlay, $Overlay$Type} from "packages/journeymap/client/api/display/$Overlay"
import {$MapPolygon, $MapPolygon$Type} from "packages/journeymap/client/api/model/$MapPolygon"
import {$ShapeProperties, $ShapeProperties$Type} from "packages/journeymap/client/api/model/$ShapeProperties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$MapPolygonWithHoles, $MapPolygonWithHoles$Type} from "packages/journeymap/client/api/model/$MapPolygonWithHoles"

export class $PolygonOverlay extends $Overlay {

constructor(arg0: string, arg1: string, arg2: $ResourceKey$Type<($Level$Type)>, arg3: $ShapeProperties$Type, arg4: $MapPolygon$Type, arg5: $List$Type<($MapPolygon$Type)>)
constructor(arg0: string, arg1: string, arg2: $ResourceKey$Type<($Level$Type)>, arg3: $ShapeProperties$Type, arg4: $MapPolygonWithHoles$Type)
constructor(arg0: string, arg1: string, arg2: $ResourceKey$Type<($Level$Type)>, arg3: $ShapeProperties$Type, arg4: $MapPolygon$Type)

public "toString"(): string
public "getHoles"(): $List<($MapPolygon)>
public "setHoles"(arg0: $List$Type<($MapPolygon$Type)>): $PolygonOverlay
public "getOuterArea"(): $MapPolygon
public "setShapeProperties"(arg0: $ShapeProperties$Type): $PolygonOverlay
public "setOuterArea"(arg0: $MapPolygon$Type): $PolygonOverlay
public "getShapeProperties"(): $ShapeProperties
public "setPolygonWithHoles"(arg0: $MapPolygonWithHoles$Type): $PolygonOverlay
get "holes"(): $List<($MapPolygon)>
set "holes"(value: $List$Type<($MapPolygon$Type)>)
get "outerArea"(): $MapPolygon
set "shapeProperties"(value: $ShapeProperties$Type)
set "outerArea"(value: $MapPolygon$Type)
get "shapeProperties"(): $ShapeProperties
set "polygonWithHoles"(value: $MapPolygonWithHoles$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolygonOverlay$Type = ($PolygonOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolygonOverlay_ = $PolygonOverlay$Type;
}}
declare module "packages/journeymap/client/ui/component/$SliderButton" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SliderButton {

 "m_7979_"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean

(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}

export namespace $SliderButton {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SliderButton$Type = ($SliderButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SliderButton_ = $SliderButton$Type;
}}
declare module "packages/journeymap/client/mod/impl/$BiomesOPlenty" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"

export class $BiomesOPlenty implements $IModBlockHandler {

constructor()

public "initialize"(arg0: $BlockMD$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomesOPlenty$Type = ($BiomesOPlenty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomesOPlenty_ = $BiomesOPlenty$Type;
}}
declare module "packages/journeymap/client/task/multi/$RenderSpec" {
import {$RenderSpec$RevealShape, $RenderSpec$RevealShape$Type} from "packages/journeymap/client/task/multi/$RenderSpec$RevealShape"

export class $RenderSpec {


public "getMaxSecondaryRenderDistance"(): integer
public "getPrimaryRenderSize"(): integer
public "getLastSecondaryRenderSize"(): integer
public "getPrimaryRenderDistance"(): integer
public "getLastSecondaryRenderDistance"(): integer
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getDebugStats"(): string
public static "getSurfaceSpec"(): $RenderSpec
public static "getTopoSpec"(): $RenderSpec
public static "getUndergroundSpec"(): $RenderSpec
public "isTopo"(): boolean
public "getSurface"(): boolean
public "isUnderground"(): boolean
public static "resetRenderSpecs"(): void
public "copyLastStatsFrom"(arg0: $RenderSpec$Type): void
public "setLastTaskInfo"(arg0: integer, arg1: long): void
public "getLastTaskChunks"(): integer
public "getRevealShape"(): $RenderSpec$RevealShape
get "maxSecondaryRenderDistance"(): integer
get "primaryRenderSize"(): integer
get "lastSecondaryRenderSize"(): integer
get "primaryRenderDistance"(): integer
get "lastSecondaryRenderDistance"(): integer
get "debugStats"(): string
get "surfaceSpec"(): $RenderSpec
get "topoSpec"(): $RenderSpec
get "undergroundSpec"(): $RenderSpec
get "topo"(): boolean
get "surface"(): boolean
get "underground"(): boolean
get "lastTaskChunks"(): integer
get "revealShape"(): $RenderSpec$RevealShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSpec$Type = ($RenderSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSpec_ = $RenderSpec$Type;
}}
declare module "packages/journeymap/client/io/$MapSaver" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"

export class $MapSaver {

constructor(arg0: $File$Type, arg1: $MapType$Type)

public "isValid"(): boolean
public "saveMap"(): $File
public "getSaveFileName"(): string
get "valid"(): boolean
get "saveFileName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapSaver$Type = ($MapSaver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapSaver_ = $MapSaver$Type;
}}
declare module "packages/journeymap/client/api/option/$KeyedEnum" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeyedEnum {

 "getKey"(): string

(): string
}

export namespace $KeyedEnum {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyedEnum$Type = ($KeyedEnum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyedEnum_ = $KeyedEnum$Type;
}}
declare module "packages/journeymap/client/mod/impl/$CreateMod" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CreateMod implements $IModBlockHandler, $IBlockColorProxy {

constructor()

public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public "initialize"(arg0: $BlockMD$Type): void
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateMod$Type = ($CreateMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateMod_ = $CreateMod$Type;
}}
declare module "packages/journeymap/client/io/$ThemeLoader" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ThemeLoader {
static readonly "THEME_FILE_SUFFIX": string
static readonly "DEFAULT_THEME_FILE": string
static readonly "GSON": $Gson

constructor()

public static "initialize"(arg0: boolean): void
public static "save"(arg0: $Theme$Type): void
public static "getThemeDirectories"(): ($File)[]
public static "preloadCurrentTheme"(): void
public static "getThemes"(): $List<($Theme)>
public static "getDefaultTheme"(): $Theme
public static "getThemeNames"(): $List<(string)>
public static "loadThemeFromFile"(arg0: $File$Type, arg1: boolean): $Theme
public static "getThemeIconDir"(): $File
public static "getCurrentTheme"(): $Theme
public static "getCurrentTheme"(arg0: boolean): $Theme
public static "getThemeByName"(arg0: string): $Theme
public static "setCurrentTheme"(arg0: $Theme$Type): void
public static "loadNextTheme"(): void
get "themeDirectories"(): ($File)[]
get "themes"(): $List<($Theme)>
get "defaultTheme"(): $Theme
get "themeNames"(): $List<(string)>
get "themeIconDir"(): $File
get "currentTheme"(): $Theme
set "currentTheme"(value: $Theme$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeLoader$Type = ($ThemeLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeLoader_ = $ThemeLoader$Type;
}}
declare module "packages/journeymap/client/command/$CmdEditWaypoint" {
import {$JMCommand, $JMCommand$Type} from "packages/journeymap/client/command/$JMCommand"
import {$CommandSource, $CommandSource$Type} from "packages/net/minecraft/commands/$CommandSource"

export class $CmdEditWaypoint implements $JMCommand {

constructor()

public "getName"(): string
public "execute"(arg0: $CommandSource$Type, arg1: (string)[]): integer
public "getUsage"(arg0: $CommandSource$Type): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdEditWaypoint$Type = ($CmdEditWaypoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdEditWaypoint_ = $CmdEditWaypoint$Type;
}}
declare module "packages/journeymap/client/event/handlers/keymapping/$UpdateAwareKeyBinding" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyModifier, $KeyModifier$Type} from "packages/journeymap/client/event/handlers/keymapping/$KeyModifier"
import {$KeyConflictContext, $KeyConflictContext$Type} from "packages/journeymap/client/event/handlers/keymapping/$KeyConflictContext"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$KeyEventHandler, $KeyEventHandler$Type} from "packages/journeymap/client/event/handlers/$KeyEventHandler"
import {$KeyModifier as $KeyModifier$0, $KeyModifier$Type as $KeyModifier$0$Type} from "packages/net/minecraftforge/client/settings/$KeyModifier"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $UpdateAwareKeyBinding extends $KeyMapping {
static readonly "ALL": $Map<(string), ($KeyMapping)>
static readonly "CATEGORY_MOVEMENT": string
static readonly "CATEGORY_MISC": string
static readonly "CATEGORY_MULTIPLAYER": string
static readonly "CATEGORY_GAMEPLAY": string
static readonly "CATEGORY_INVENTORY": string
static readonly "CATEGORY_INTERFACE": string
static readonly "CATEGORY_CREATIVE": string
 "key": $InputConstants$Key
 "clickCount": integer

constructor(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifier$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string, arg6: $KeyEventHandler$Type)

public "getKey"(): $InputConstants$Key
public "setKeyModifierAndCode"(arg0: $KeyModifier$0$Type, arg1: $InputConstants$Key$Type): void
public "getConflictContext"(): $KeyConflictContext
public "isActiveAndMatches"(arg0: $InputConstants$Key$Type): boolean
public "setKey"(arg0: $InputConstants$Key$Type): void
public "getModifier"(): $KeyModifier
get "key"(): $InputConstants$Key
get "conflictContext"(): $KeyConflictContext
set "key"(value: $InputConstants$Key$Type)
get "modifier"(): $KeyModifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateAwareKeyBinding$Type = ($UpdateAwareKeyBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateAwareKeyBinding_ = $UpdateAwareKeyBinding$Type;
}}
declare module "packages/journeymap/client/ui/component/$ButtonList" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$Button as $Button$0, $Button$Type as $Button$0$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ButtonList$Direction, $ButtonList$Direction$Type} from "packages/journeymap/client/ui/component/$ButtonList$Direction"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ButtonList$Layout, $ButtonList$Layout$Type} from "packages/journeymap/client/ui/component/$ButtonList$Layout"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ButtonList extends $ArrayList<($Button)> {

constructor(...arg0: ($Button$Type)[])
constructor()
constructor(arg0: string)
constructor(arg0: $List$Type<($Button$0$Type)>)

public "setDrawButtons"(arg0: boolean): void
public "setFitWidths"(arg0: $Font$Type): void
public "reverse"(): $ButtonList
public "getTopY"(): integer
public "equalizeWidths"(arg0: $Font$Type): void
public "equalizeWidths"(arg0: $Font$Type, arg1: integer, arg2: integer): void
public "getBottomY"(): integer
public "setWidths"(arg0: integer): void
public "setHeights"(arg0: integer): void
public "getLeftX"(): integer
public "layoutVertical"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "setEnabled"(arg0: boolean): $ButtonList
public "fitWidths"(arg0: $Font$Type): void
public "setVisible"(arg0: boolean): $ButtonList
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): $ButtonList
public "getWidth"(arg0: integer): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "getHeight"(arg0: integer): integer
public "isHorizontal"(): boolean
public "setLayout"(arg0: $ButtonList$Layout$Type, arg1: $ButtonList$Direction$Type): void
public "setDefaultStyle"(arg0: boolean): $ButtonList
public "setLabel"(arg0: string): void
public "layoutFilledHorizontal"(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean): $ButtonList
public "getRightX"(): integer
public "layoutHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "layoutHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer, arg4: boolean): $ButtonList
public "setOptions"(arg0: boolean, arg1: boolean, arg2: boolean): $ButtonList
public "getLabel"(): string
public "layoutCenteredHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer, arg4: boolean): $ButtonList
public "layoutCenteredHorizontal"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "layoutCenteredVertical"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): $ButtonList
public "layoutDistributedHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): $ButtonList
public "getVisibleButtonCount"(): integer
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
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
set "drawButtons"(value: boolean)
get "topY"(): integer
get "bottomY"(): integer
set "widths"(value: integer)
set "heights"(value: integer)
get "leftX"(): integer
set "enabled"(value: boolean)
set "visible"(value: boolean)
get "width"(): integer
get "height"(): integer
get "horizontal"(): boolean
set "defaultStyle"(value: boolean)
set "label"(value: string)
get "rightX"(): integer
get "label"(): string
get "visibleButtonCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonList$Type = ($ButtonList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonList_ = $ButtonList$Type;
}}
declare module "packages/journeymap/client/cartography/render/$EndCaveRenderer" {
import {$Stratum, $Stratum$Type} from "packages/journeymap/client/cartography/$Stratum"
import {$IChunkRenderer, $IChunkRenderer$Type} from "packages/journeymap/client/cartography/$IChunkRenderer"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$SurfaceRenderer, $SurfaceRenderer$Type} from "packages/journeymap/client/cartography/render/$SurfaceRenderer"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$CaveRenderer, $CaveRenderer$Type} from "packages/journeymap/client/cartography/render/$CaveRenderer"
import {$ComparableNativeImage, $ComparableNativeImage$Type} from "packages/journeymap/client/texture/$ComparableNativeImage"

export class $EndCaveRenderer extends $CaveRenderer implements $IChunkRenderer {
static readonly "COLOR_BLACK": integer
static "badBlockCount": $AtomicLong

constructor(arg0: $SurfaceRenderer$Type)

public "render"(arg0: $ComparableNativeImage$Type, arg1: $RegionData$Type, arg2: $ChunkMD$Type, arg3: integer): boolean
public "setStratumColors"(arg0: $Stratum$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean): void
public "getAmbientColor"(): (float)[]
get "ambientColor"(): (float)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EndCaveRenderer$Type = ($EndCaveRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EndCaveRenderer_ = $EndCaveRenderer$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawUtil$VAlign" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DrawUtil$VAlign extends $Enum<($DrawUtil$VAlign)> {
static readonly "Above": $DrawUtil$VAlign
static readonly "Middle": $DrawUtil$VAlign
static readonly "Below": $DrawUtil$VAlign


public static "values"(): ($DrawUtil$VAlign)[]
public static "valueOf"(arg0: string): $DrawUtil$VAlign
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawUtil$VAlign$Type = (("middle") | ("below") | ("above")) | ($DrawUtil$VAlign);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawUtil$VAlign_ = $DrawUtil$VAlign$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$Fullscreen" {
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"
import {$Theme$LabelSpec, $Theme$LabelSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$LabelSpec"

export class $Theme$Fullscreen {
 "background": $Theme$ColorSpec
 "statusLabel": $Theme$LabelSpec

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Fullscreen$Type = ($Theme$Fullscreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$Fullscreen_ = $Theme$Fullscreen$Type;
}}
declare module "packages/journeymap/client/properties/$MiniMapProperties" {
import {$Shape, $Shape$Type} from "packages/journeymap/client/ui/minimap/$Shape"
import {$Orientation, $Orientation$Type} from "packages/journeymap/client/ui/minimap/$Orientation"
import {$StringField, $StringField$Type} from "packages/journeymap/common/properties/config/$StringField"
import {$ReticleOrientation, $ReticleOrientation$Type} from "packages/journeymap/client/ui/minimap/$ReticleOrientation"
import {$EntityDisplay, $EntityDisplay$Type} from "packages/journeymap/client/ui/minimap/$EntityDisplay"
import {$FloatField, $FloatField$Type} from "packages/journeymap/common/properties/config/$FloatField"
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$Position, $Position$Type} from "packages/journeymap/client/ui/minimap/$Position"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"
import {$InGameMapProperties, $InGameMapProperties$Type} from "packages/journeymap/client/properties/$InGameMapProperties"

export class $MiniMapProperties extends $InGameMapProperties {
readonly "gameTimeRealFormat": $StringField
readonly "systemTimeRealFormat": $StringField
readonly "enabled": $BooleanField
readonly "shape": $EnumField<($Shape)>
readonly "showDayNight": $BooleanField
readonly "info1Label": $StringField
readonly "info2Label": $StringField
readonly "info3Label": $StringField
readonly "info4Label": $StringField
readonly "infoSlotAlpha": $FloatField
readonly "positionX": $FloatField
readonly "positionY": $FloatField
readonly "sizePercent": $IntegerField
readonly "frameAlpha": $IntegerField
readonly "terrainAlpha": $IntegerField
readonly "backgroundAlpha": $FloatField
readonly "orientation": $EnumField<($Orientation)>
readonly "compassFontScale": $FloatField
readonly "showCompass": $BooleanField
readonly "showReticle": $BooleanField
readonly "reticleOrientation": $EnumField<($ReticleOrientation)>
readonly "moveEffectIcons": $BooleanField
readonly "effectTranslateX": $IntegerField
readonly "effectTranslateY": $IntegerField
readonly "effectVertical": $BooleanField
readonly "effectReversed": $BooleanField
readonly "minimapKeyMovementSpeed": $FloatField
readonly "position": $EnumField<($Position)>
readonly "playerDisplay": $EnumField<($EntityDisplay)>
readonly "selfDisplayScale": $FloatField
readonly "playerDisplayScale": $FloatField
readonly "showPlayerHeading": $BooleanField
readonly "mobDisplay": $EnumField<($EntityDisplay)>
readonly "mobDisplayScale": $FloatField
readonly "showMobHeading": $BooleanField
readonly "showMobs": $BooleanField
readonly "showAnimals": $BooleanField
readonly "showVillagers": $BooleanField
readonly "showPets": $BooleanField
readonly "showPlayers": $BooleanField
readonly "fontScale": $FloatField
readonly "showWaypointLabels": $BooleanField
readonly "waypointLabelScale": $FloatField
readonly "waypointIconScale": $FloatField
readonly "locationFormatVerbose": $BooleanField
readonly "locationFormat": $StringField
readonly "showWaypoints": $BooleanField
readonly "showSelf": $BooleanField
readonly "showGrid": $BooleanField
readonly "showCaves": $BooleanField
readonly "showEntityNames": $BooleanField
readonly "preferredMapType": $EnumField<($MapType$Name)>
readonly "zoomLevel": $IntegerField

constructor(arg0: integer)

public "getName"(): string
public "getId"(): integer
public "getSize"(): integer
public "isActive"(): boolean
public "setActive"(arg0: boolean): void
public "updateFrom"<T extends $PropertiesBase>(arg0: T): void
get "name"(): string
get "id"(): integer
get "size"(): integer
get "active"(): boolean
set "active"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MiniMapProperties$Type = ($MiniMapProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MiniMapProperties_ = $MiniMapProperties$Type;
}}
declare module "packages/journeymap/client/ui/component/$Removable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Removable {

 "onRemove"(): void

(): void
}

export namespace $Removable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Removable$Type = ($Removable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Removable_ = $Removable$Type;
}}
declare module "packages/journeymap/client/ui/component/$DropDownButton" {
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SelectableParent, $SelectableParent$Type} from "packages/journeymap/client/ui/component/$SelectableParent"
import {$DropDownItem, $DropDownItem$Type} from "packages/journeymap/client/ui/component/$DropDownItem"
import {$Removable, $Removable$Type} from "packages/journeymap/client/ui/component/$Removable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $DropDownButton extends $Button implements $Removable, $SelectableParent {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: string, arg1: $Button$OnPress$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setItems"(arg0: $List$Type<($DropDownItem$Type)>): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "setSelected"(arg0: $DropDownItem$Type): void
public "getWidth"(): integer
public "onRemove"(): void
set "items"(value: $List$Type<($DropDownItem$Type)>)
set "selected"(value: $DropDownItem$Type)
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropDownButton$Type = ($DropDownButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropDownButton_ = $DropDownButton$Type;
}}
declare module "packages/journeymap/client/model/$SplashInfo" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$SplashInfo$Line, $SplashInfo$Line$Type} from "packages/journeymap/client/model/$SplashInfo$Line"

export class $SplashInfo {
 "lines": $ArrayList<($SplashInfo$Line)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SplashInfo$Type = ($SplashInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SplashInfo_ = $SplashInfo$Type;
}}
declare module "packages/journeymap/client/task/multi/$MapPlayerTask$MapPlayerTaskBatch" {
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TaskBatch, $TaskBatch$Type} from "packages/journeymap/client/task/multi/$TaskBatch"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"

export class $MapPlayerTask$MapPlayerTaskBatch extends $TaskBatch {

constructor(arg0: $List$Type<($ITask$Type)>)

public "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapPlayerTask$MapPlayerTaskBatch$Type = ($MapPlayerTask$MapPlayerTaskBatch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapPlayerTask$MapPlayerTaskBatch_ = $MapPlayerTask$MapPlayerTaskBatch$Type;
}}
declare module "packages/journeymap/client/mod/$IModBlockHandler" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"

export interface $IModBlockHandler {

 "initialize"(arg0: $BlockMD$Type): void

(arg0: $BlockMD$Type): void
}

export namespace $IModBlockHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModBlockHandler$Type = ($IModBlockHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModBlockHandler_ = $IModBlockHandler$Type;
}}
declare module "packages/journeymap/client/ui/component/$CheckBox" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$BooleanPropertyButton, $BooleanPropertyButton$Type} from "packages/journeymap/client/ui/component/$BooleanPropertyButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $CheckBox extends $BooleanPropertyButton {
 "boxWidth": integer
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: string, arg1: $BooleanField$Type)
constructor(arg0: string, arg1: boolean)
constructor(arg0: string, arg1: $BooleanField$Type, arg2: $Button$OnPress$Type)
constructor(arg0: string, arg1: boolean, arg2: $Button$OnPress$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getFitWidth"(arg0: $Font$Type): integer
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "keyTyped"(arg0: character, arg1: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckBox$Type = ($CheckBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckBox_ = $CheckBox$Type;
}}
declare module "packages/journeymap/client/mod/impl/$ImmersiveRailroading" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"
import {$IBlockColorProxy, $IBlockColorProxy$Type} from "packages/journeymap/client/mod/$IBlockColorProxy"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ImmersiveRailroading implements $IModBlockHandler, $IBlockColorProxy {

constructor()

public "deriveBlockColor"(arg0: $BlockMD$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): integer
public "initialize"(arg0: $BlockMD$Type): void
public "getBlockColor"(arg0: $ChunkMD$Type, arg1: $BlockMD$Type, arg2: $BlockPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmersiveRailroading$Type = ($ImmersiveRailroading);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmersiveRailroading_ = $ImmersiveRailroading$Type;
}}
declare module "packages/journeymap/client/event/forge/$ForgeLoggedInEvent" {
import {$ForgeEventHandlerManager$EventHandler, $ForgeEventHandlerManager$EventHandler$Type} from "packages/journeymap/client/event/forge/$ForgeEventHandlerManager$EventHandler"
import {$ClientPlayerNetworkEvent$LoggingIn, $ClientPlayerNetworkEvent$LoggingIn$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingIn"

export class $ForgeLoggedInEvent implements $ForgeEventHandlerManager$EventHandler {

constructor()

public "onConnect"(arg0: $ClientPlayerNetworkEvent$LoggingIn$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeLoggedInEvent$Type = ($ForgeLoggedInEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeLoggedInEvent_ = $ForgeLoggedInEvent$Type;
}}
declare module "packages/journeymap/client/$JourneymapClient" {
import {$ClientNetworkDispatcher, $ClientNetworkDispatcher$Type} from "packages/journeymap/common/network/dispatch/$ClientNetworkDispatcher"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$FullMapProperties, $FullMapProperties$Type} from "packages/journeymap/client/properties/$FullMapProperties"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Webmap, $Webmap$Type} from "packages/journeymap/client/service/webmap/$Webmap"
import {$ClientPacketHandler, $ClientPacketHandler$Type} from "packages/journeymap/common/network/handler/$ClientPacketHandler"
import {$TopoProperties, $TopoProperties$Type} from "packages/journeymap/client/properties/$TopoProperties"
import {$InternalStateHandler, $InternalStateHandler$Type} from "packages/journeymap/client/$InternalStateHandler"
import {$CoreProperties, $CoreProperties$Type} from "packages/journeymap/client/properties/$CoreProperties"
import {$IMainThreadTask, $IMainThreadTask$Type} from "packages/journeymap/client/task/main/$IMainThreadTask"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$ForgeKeyEvents, $ForgeKeyEvents$Type} from "packages/journeymap/client/event/forge/$ForgeKeyEvents"
import {$ChunkRenderController, $ChunkRenderController$Type} from "packages/journeymap/client/cartography/$ChunkRenderController"
import {$WaypointProperties, $WaypointProperties$Type} from "packages/journeymap/client/properties/$WaypointProperties"
import {$MiniMapProperties, $MiniMapProperties$Type} from "packages/journeymap/client/properties/$MiniMapProperties"
import {$WebMapProperties, $WebMapProperties$Type} from "packages/journeymap/client/properties/$WebMapProperties"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$FMLLoadCompleteEvent, $FMLLoadCompleteEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLLoadCompleteEvent"

export class $JourneymapClient {
static readonly "FULL_VERSION": string
static readonly "MOD_NAME": string
 "hasOptifine": boolean

constructor()

public static "getInstance"(): $JourneymapClient
public "enable"(): void
public "getDispatcher"(): $ClientNetworkDispatcher
public "clientSetupEvent"(arg0: $FMLClientSetupEvent$Type): void
public "loadCompleteEvent"(arg0: $FMLLoadCompleteEvent$Type): void
public "getCoreProperties"(): $CoreProperties
public "getTopoProperties"(): $TopoProperties
public "isMapping"(): boolean
public "toggleTask"(arg0: $Class$Type<(any)>, arg1: boolean, arg2: any): void
public "queueOneOff"(arg0: $Runnable$Type): void
public "isThreadLogging"(): boolean
public "startMapping"(): void
public "stopMapping"(): void
public "getJmServer"(): $Webmap
public "getActiveMinimapId"(): integer
public "getCurrentWorldId"(): string
public "getKeyEvents"(): $ForgeKeyEvents
public "setCurrentWorldId"(arg0: string): void
public "getRenderDistance"(): integer
public "getStateHandler"(): $InternalStateHandler
public "commonSetupEvent"(arg0: $FMLCommonSetupEvent$Type): void
public "getPacketHandler"(): $ClientPacketHandler
public "queueMainThreadTask"(arg0: $IMainThreadTask$Type): void
public "loadConfigProperties"(): void
public "isUpdateCheckEnabled"(): boolean
public "isMainThreadTaskActive"(): boolean
public "getActiveMiniMapProperties"(): $MiniMapProperties
public "getMiniMapProperties1"(): $MiniMapProperties
public "getFullMapProperties"(): $FullMapProperties
public "isTaskManagerEnabled"(arg0: $Class$Type<(any)>): boolean
public "getMiniMapProperties"(arg0: integer): $MiniMapProperties
public "getWebMapProperties"(): $WebMapProperties
public "getMiniMapProperties2"(): $MiniMapProperties
public "getWaypointProperties"(): $WaypointProperties
public "getChunkRenderController"(): $ChunkRenderController
public "performMainThreadTasks"(): void
public "saveConfigProperties"(): void
public "performMultithreadTasks"(): void
public "disable"(): void
public "isInitialized"(): boolean
get "instance"(): $JourneymapClient
get "dispatcher"(): $ClientNetworkDispatcher
get "coreProperties"(): $CoreProperties
get "topoProperties"(): $TopoProperties
get "mapping"(): boolean
get "threadLogging"(): boolean
get "jmServer"(): $Webmap
get "activeMinimapId"(): integer
get "currentWorldId"(): string
get "keyEvents"(): $ForgeKeyEvents
set "currentWorldId"(value: string)
get "renderDistance"(): integer
get "stateHandler"(): $InternalStateHandler
get "packetHandler"(): $ClientPacketHandler
get "updateCheckEnabled"(): boolean
get "mainThreadTaskActive"(): boolean
get "activeMiniMapProperties"(): $MiniMapProperties
get "miniMapProperties1"(): $MiniMapProperties
get "fullMapProperties"(): $FullMapProperties
get "webMapProperties"(): $WebMapProperties
get "miniMapProperties2"(): $MiniMapProperties
get "waypointProperties"(): $WaypointProperties
get "chunkRenderController"(): $ChunkRenderController
get "initialized"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JourneymapClient$Type = ($JourneymapClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JourneymapClient_ = $JourneymapClient$Type;
}}
declare module "packages/journeymap/client/task/multi/$ITaskManager" {
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ITaskManager {

 "isEnabled"(arg0: $Minecraft$Type): boolean
 "getTask"(arg0: $Minecraft$Type): $ITask
 "enableTask"(arg0: $Minecraft$Type, arg1: any): boolean
 "getTaskClass"(): $Class<(any)>
 "taskAccepted"(arg0: $ITask$Type, arg1: boolean): void
 "disableTask"(arg0: $Minecraft$Type): void
}

export namespace $ITaskManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITaskManager$Type = ($ITaskManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITaskManager_ = $ITaskManager$Type;
}}
declare module "packages/journeymap/client/event/handlers/keymapping/$KeyBindingAction" {
import {$UpdateAwareKeyBinding, $UpdateAwareKeyBinding$Type} from "packages/journeymap/client/event/handlers/keymapping/$UpdateAwareKeyBinding"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $KeyBindingAction {

constructor(arg0: $UpdateAwareKeyBinding$Type, arg1: $Runnable$Type)

public "toString"(): string
public "isActive"(arg0: integer, arg1: boolean, arg2: $InputConstants$Type$Type): boolean
public "order"(): integer
public "getKeyBinding"(): $UpdateAwareKeyBinding
public "getAction"(): $Runnable
get "keyBinding"(): $UpdateAwareKeyBinding
get "action"(): $Runnable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyBindingAction$Type = ($KeyBindingAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyBindingAction_ = $KeyBindingAction$Type;
}}
declare module "packages/journeymap/client/ui/component/$OptionsScrollListPane" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$ScrollListPane, $ScrollListPane$Type} from "packages/journeymap/client/ui/component/$ScrollListPane"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Slot, $Slot$Type} from "packages/journeymap/client/ui/component/$Slot"

export class $OptionsScrollListPane<T extends $Slot> extends $ScrollListPane<(T)> {
 "lastTooltipMetadata": $SlotMetadata<(any)>
 "lastTooltip": $List<($FormattedCharSequence)>
 "lastTooltipTime": long
 "hoverDelay": long
 "scrolling": boolean
 "hovered": E

constructor(arg0: $JmUI$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public "m_239227_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsScrollListPane$Type<T> = ($OptionsScrollListPane<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsScrollListPane_<T> = $OptionsScrollListPane$Type<(T)>;
}}
declare module "packages/journeymap/client/task/multi/$SaveMapTask" {
import {$ITask, $ITask$Type} from "packages/journeymap/client/task/multi/$ITask"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$File, $File$Type} from "packages/java/io/$File"
import {$JourneymapClient, $JourneymapClient$Type} from "packages/journeymap/client/$JourneymapClient"

export class $SaveMapTask implements $ITask {
static "MAP_TYPE": $MapType


public "performTask"(arg0: $Minecraft$Type, arg1: $JourneymapClient$Type, arg2: $File$Type, arg3: boolean): void
public "getMaxRuntime"(): integer
get "maxRuntime"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SaveMapTask$Type = ($SaveMapTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SaveMapTask_ = $SaveMapTask$Type;
}}
declare module "packages/journeymap/client/properties/$InGameMapProperties" {
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$MapProperties, $MapProperties$Type} from "packages/journeymap/client/properties/$MapProperties"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$StringField, $StringField$Type} from "packages/journeymap/common/properties/config/$StringField"
import {$EntityDisplay, $EntityDisplay$Type} from "packages/journeymap/client/ui/minimap/$EntityDisplay"
import {$FloatField, $FloatField$Type} from "packages/journeymap/common/properties/config/$FloatField"

export class $InGameMapProperties extends $MapProperties {
readonly "playerDisplay": $EnumField<($EntityDisplay)>
readonly "selfDisplayScale": $FloatField
readonly "playerDisplayScale": $FloatField
readonly "showPlayerHeading": $BooleanField
readonly "mobDisplay": $EnumField<($EntityDisplay)>
readonly "mobDisplayScale": $FloatField
readonly "showMobHeading": $BooleanField
readonly "showMobs": $BooleanField
readonly "showAnimals": $BooleanField
readonly "showVillagers": $BooleanField
readonly "showPets": $BooleanField
readonly "showPlayers": $BooleanField
readonly "fontScale": $FloatField
readonly "showWaypointLabels": $BooleanField
readonly "waypointLabelScale": $FloatField
readonly "waypointIconScale": $FloatField
readonly "locationFormatVerbose": $BooleanField
readonly "locationFormat": $StringField
readonly "showWaypoints": $BooleanField
readonly "showSelf": $BooleanField
readonly "showGrid": $BooleanField
readonly "showCaves": $BooleanField
readonly "showEntityNames": $BooleanField
readonly "preferredMapType": $EnumField<($MapType$Name)>
readonly "zoomLevel": $IntegerField


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InGameMapProperties$Type = ($InGameMapProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InGameMapProperties_ = $InGameMapProperties$Type;
}}
declare module "packages/journeymap/client/api/impl/$BlockInfo" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$IBlockInfo, $IBlockInfo$Type} from "packages/journeymap/client/api/model/$IBlockInfo"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockInfo$Builder, $BlockInfo$Builder$Type} from "packages/journeymap/client/api/impl/$BlockInfo$Builder"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockInfo implements $IBlockInfo {


public "getRegionZ"(): integer
public "getRegionX"(): integer
public static "builder"(): $BlockInfo$Builder
public "getBlock"(): $Block
public "getBiome"(): $Biome
public "getChunkPos"(): $ChunkPos
public "getBlockPos"(): $BlockPos
public "getBlockState"(): $BlockState
public "getChunk"(): $LevelChunk
get "regionZ"(): integer
get "regionX"(): integer
get "block"(): $Block
get "biome"(): $Biome
get "chunkPos"(): $ChunkPos
get "blockPos"(): $BlockPos
get "blockState"(): $BlockState
get "chunk"(): $LevelChunk
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInfo$Type = ($BlockInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInfo_ = $BlockInfo$Type;
}}
declare module "packages/journeymap/client/render/draw/$RadarDrawStepFactory" {
import {$EntityDTO, $EntityDTO$Type} from "packages/journeymap/client/model/$EntityDTO"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DrawStep, $DrawStep$Type} from "packages/journeymap/client/render/draw/$DrawStep"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$InGameMapProperties, $InGameMapProperties$Type} from "packages/journeymap/client/properties/$InGameMapProperties"

export class $RadarDrawStepFactory {

constructor()

public "prepareSteps"(arg0: $List$Type<($EntityDTO$Type)>, arg1: $GridRenderer$Type, arg2: $InGameMapProperties$Type): $List<($DrawStep)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RadarDrawStepFactory$Type = ($RadarDrawStepFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RadarDrawStepFactory_ = $RadarDrawStepFactory$Type;
}}
declare module "packages/journeymap/client/ui/theme/$IThemeToolbarInternal" {
import {$IThemeToolBar, $IThemeToolBar$Type} from "packages/journeymap/client/api/display/$IThemeToolBar"

export interface $IThemeToolbarInternal extends $IThemeToolBar {

/**
 * 
 * @deprecated
 */
 "m_93694_"(): integer
/**
 * 
 * @deprecated
 */
 "m_5711_"(): integer
/**
 * 
 * @deprecated
 */
 "m_252754_"(): integer
/**
 * 
 * @deprecated
 */
 "m_252907_"(): integer
/**
 * 
 * @deprecated
 */
 "m_264152_"(arg0: integer, arg1: integer): void
 "getToolbarHeight"(): integer
 "getToolbarX"(): integer
 "getToolbarY"(): integer
 "setToolbarPosition"(arg0: integer, arg1: integer): void
 "getToolbarWidth"(): integer
 "getBottomY"(): integer
 "getY"(): integer
 "getX"(): integer
 "getWidth"(): integer
 "getHeight"(): integer
 "getMiddleY"(): integer
 "setPosition"(arg0: integer, arg1: integer): void
 "getCenterX"(): integer
 "getRightX"(): integer
 "isMouseOver"(): boolean
 "setLayoutVertical"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setReverse"(): void
 "setLayoutDistributedHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setLayoutCenteredHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setLayoutHorizontal"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
 "setLayoutCenteredVertical"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
}

export namespace $IThemeToolbarInternal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IThemeToolbarInternal$Type = ($IThemeToolbarInternal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IThemeToolbarInternal_ = $IThemeToolbarInternal$Type;
}}
declare module "packages/journeymap/client/api/event/forge/$FullscreenDisplayEvent" {
import {$IFullscreen, $IFullscreen$Type} from "packages/journeymap/client/api/model/$IFullscreen"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$ThemeButtonDisplay, $ThemeButtonDisplay$Type} from "packages/journeymap/client/api/display/$ThemeButtonDisplay"

export class $FullscreenDisplayEvent extends $Event {

constructor()

public "isCancelable"(): boolean
public "getThemeButtonDisplay"(): $ThemeButtonDisplay
public "getFullscreen"(): $IFullscreen
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "themeButtonDisplay"(): $ThemeButtonDisplay
get "fullscreen"(): $IFullscreen
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullscreenDisplayEvent$Type = ($FullscreenDisplayEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullscreenDisplayEvent_ = $FullscreenDisplayEvent$Type;
}}
declare module "packages/journeymap/client/ui/theme/$Theme$ImageSpec" {
import {$Theme$ColorSpec, $Theme$ColorSpec$Type} from "packages/journeymap/client/ui/theme/$Theme$ColorSpec"

export class $Theme$ImageSpec extends $Theme$ColorSpec {
 "width": integer
 "height": integer
 "color": string
 "alpha": float

constructor()
constructor(arg0: integer, arg1: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$ImageSpec$Type = ($Theme$ImageSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme$ImageSpec_ = $Theme$ImageSpec$Type;
}}
declare module "packages/journeymap/client/model/$BlockDataArrays$Dataset" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$BlockDataArrays$DataArray, $BlockDataArrays$DataArray$Type} from "packages/journeymap/client/model/$BlockDataArrays$DataArray"

export class $BlockDataArrays$Dataset {

constructor(arg0: $MapType$Type)

public "ints"(): $BlockDataArrays$DataArray<(integer)>
public "objects"(): $BlockDataArrays$DataArray<(any)>
public "floats"(): $BlockDataArrays$DataArray<(float)>
public "booleans"(): $BlockDataArrays$DataArray<(boolean)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDataArrays$Dataset$Type = ($BlockDataArrays$Dataset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDataArrays$Dataset_ = $BlockDataArrays$Dataset$Type;
}}
declare module "packages/journeymap/client/model/$MapState" {
import {$MapProperties, $MapProperties$Type} from "packages/journeymap/client/properties/$MapProperties"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RadarDrawStepFactory, $RadarDrawStepFactory$Type} from "packages/journeymap/client/render/draw/$RadarDrawStepFactory"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$DrawWayPointStep, $DrawWayPointStep$Type} from "packages/journeymap/client/render/draw/$DrawWayPointStep"
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AtomicBoolean, $AtomicBoolean$Type} from "packages/java/util/concurrent/atomic/$AtomicBoolean"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$WaypointDrawStepFactory, $WaypointDrawStepFactory$Type} from "packages/journeymap/client/render/draw/$WaypointDrawStepFactory"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$GridRenderer, $GridRenderer$Type} from "packages/journeymap/client/render/map/$GridRenderer"
import {$InGameMapProperties, $InGameMapProperties$Type} from "packages/journeymap/client/properties/$InGameMapProperties"

export class $MapState {
readonly "minZoom": integer
readonly "maxZoom": integer
 "follow": $AtomicBoolean
 "playerLastPos": string

constructor()

public "refresh"(arg0: $Minecraft$Type, arg1: $Player$Type, arg2: $InGameMapProperties$Type): void
public "getNextMapType"(arg0: $MapType$Name$Type): $MapType$Name
public "getDimension"(): $ResourceKey<($Level)>
public "requireRefresh"(): void
public "zoomIn"(): boolean
public "zoomOut"(): boolean
public "toggleMapType"(): $MapType
public "getMapType"(): $MapType
public "setMapType"(arg0: $MapType$Type): $MapType
public "setMapType"(arg0: $MapType$Name$Type): $MapType
public "getLastSlice"(): $IntegerField
public "getLastMapTypeChange"(): long
public "isCaveMappingEnabled"(): boolean
public "isUnderground"(): boolean
public "setZoom"(arg0: integer): boolean
public "isHighQuality"(): boolean
public "getZoom"(): integer
public "getWorldDir"(): $File
public "resetMapType"(): void
public "getDrawSteps"(): $List<(any)>
public "generateDrawSteps"(arg0: $Minecraft$Type, arg1: $GridRenderer$Type, arg2: $WaypointDrawStepFactory$Type, arg3: $RadarDrawStepFactory$Type, arg4: $InGameMapProperties$Type, arg5: boolean): void
public "getPlayerBiome"(): string
public "updateLastRefresh"(): void
public "shouldRefresh"(arg0: $Minecraft$Type, arg1: $MapProperties$Type): boolean
public "isCaveMappingAllowed"(): boolean
public "isTopoMappingAllowed"(): boolean
public "isBiomeMappingAllowed"(): boolean
public "isSurfaceMappingAllowed"(): boolean
public "getDrawWaypointSteps"(): $List<($DrawWayPointStep)>
public "setForceRefreshState"(arg0: boolean): void
get "dimension"(): $ResourceKey<($Level)>
get "mapType"(): $MapType
set "mapType"(value: $MapType$Type)
set "mapType"(value: $MapType$Name$Type)
get "lastSlice"(): $IntegerField
get "lastMapTypeChange"(): long
get "caveMappingEnabled"(): boolean
get "underground"(): boolean
set "zoom"(value: integer)
get "highQuality"(): boolean
get "zoom"(): integer
get "worldDir"(): $File
get "drawSteps"(): $List<(any)>
get "playerBiome"(): string
get "caveMappingAllowed"(): boolean
get "topoMappingAllowed"(): boolean
get "biomeMappingAllowed"(): boolean
get "surfaceMappingAllowed"(): boolean
get "drawWaypointSteps"(): $List<($DrawWayPointStep)>
set "forceRefreshState"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapState$Type = ($MapState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapState_ = $MapState$Type;
}}
declare module "packages/journeymap/client/ui/component/$TextFieldButton" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$IConfigFieldHolder, $IConfigFieldHolder$Type} from "packages/journeymap/client/ui/component/$IConfigFieldHolder"
import {$CustomField, $CustomField$Type} from "packages/journeymap/common/properties/config/$CustomField"
import {$TextBoxButton, $TextBoxButton$Type} from "packages/journeymap/client/ui/component/$TextBoxButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $TextFieldButton extends $TextBoxButton implements $IConfigFieldHolder<($CustomField)> {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $CustomField$Type)

public "setValue"(arg0: any): void
public "refresh"(): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getFitWidth"(arg0: $Font$Type): integer
public "charTyped"(arg0: character, arg1: integer): boolean
public "updateValue"(arg0: any): void
set "value"(value: any)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextFieldButton$Type = ($TextFieldButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextFieldButton_ = $TextFieldButton$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeToggle" {
import {$ThemeButton, $ThemeButton$Type} from "packages/journeymap/client/ui/theme/$ThemeButton"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$Theme, $Theme$Type} from "packages/journeymap/client/ui/theme/$Theme"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $ThemeToggle extends $ThemeButton {
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Theme$Type, arg1: string, arg2: string, arg3: $BooleanField$Type, arg4: $Button$OnPress$Type)
constructor(arg0: $Theme$Type, arg1: string, arg2: string, arg3: string, arg4: $Button$OnPress$Type)
constructor(arg0: $Theme$Type, arg1: string, arg2: string, arg3: $Button$OnPress$Type)

public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "setEnabled"(arg0: boolean): void
public "isActive"(): boolean
public "setTooltip"(...arg0: (string)[]): void
public "setLabels"(arg0: string, arg1: string): void
public "setDrawButton"(arg0: boolean): void
public "setToggled"(arg0: boolean): void
public "getToggled"(): boolean
public "toggle"(): void
set "enabled"(value: boolean)
get "active"(): boolean
set "tooltip"(value: (string)[])
set "drawButton"(value: boolean)
set "toggled"(value: boolean)
get "toggled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeToggle$Type = ($ThemeToggle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeToggle_ = $ThemeToggle$Type;
}}
declare module "packages/journeymap/client/mod/impl/$Streams" {
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$IModBlockHandler, $IModBlockHandler$Type} from "packages/journeymap/client/mod/$IModBlockHandler"

export class $Streams implements $IModBlockHandler {

constructor()

public "initialize"(arg0: $BlockMD$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Streams$Type = ($Streams);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Streams_ = $Streams$Type;
}}
declare module "packages/journeymap/client/ui/theme/$ThemeLabelSource" {
import {$StringField$ValuesProvider, $StringField$ValuesProvider$Type} from "packages/journeymap/common/properties/config/$StringField$ValuesProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ThemeLabelSource$InfoSlot, $ThemeLabelSource$InfoSlot$Type} from "packages/journeymap/client/ui/theme/$ThemeLabelSource$InfoSlot"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ThemeLabelSource implements $StringField$ValuesProvider {
static readonly "values": $Map<(string), ($ThemeLabelSource$InfoSlot)>
static "FPS": $ThemeLabelSource$InfoSlot
static "GameTime": $ThemeLabelSource$InfoSlot
static "GameTimeReal": $ThemeLabelSource$InfoSlot
static "RealTime": $ThemeLabelSource$InfoSlot
static "Location": $ThemeLabelSource$InfoSlot
static "Biome": $ThemeLabelSource$InfoSlot
static "Dimension": $ThemeLabelSource$InfoSlot
static "Region": $ThemeLabelSource$InfoSlot
static "LightLevel": $ThemeLabelSource$InfoSlot
static "MoonPhase": $ThemeLabelSource$InfoSlot
static "Blank": $ThemeLabelSource$InfoSlot

constructor()

public static "create"(arg0: string, arg1: string, arg2: long, arg3: long, arg4: $Supplier$Type<(string)>): $ThemeLabelSource$InfoSlot
public static "create"(arg0: string, arg1: long, arg2: long, arg3: $Supplier$Type<(string)>): $ThemeLabelSource$InfoSlot
public "getStrings"(): $List<(string)>
public "getDefaultString"(): string
public static "resetCaches"(): void
get "strings"(): $List<(string)>
get "defaultString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeLabelSource$Type = ($ThemeLabelSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeLabelSource_ = $ThemeLabelSource$Type;
}}
declare module "packages/journeymap/client/api/option/$FloatOption" {
import {$Option, $Option$Type} from "packages/journeymap/client/api/option/$Option"
import {$OptionCategory, $OptionCategory$Type} from "packages/journeymap/client/api/option/$OptionCategory"

export class $FloatOption extends $Option<(float)> {

constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: float, arg4: float, arg5: float)
constructor(arg0: $OptionCategory$Type, arg1: string, arg2: string, arg3: float, arg4: float, arg5: float, arg6: float, arg7: integer)

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
export type $FloatOption$Type = ($FloatOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatOption_ = $FloatOption$Type;
}}
declare module "packages/journeymap/client/event/handlers/$ChatEventHandler" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export class $ChatEventHandler {

constructor()

public "onClientChatEventReceived"(arg0: $Component$Type): $Component
public "onChatEvent"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatEventHandler$Type = ($ChatEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatEventHandler_ = $ChatEventHandler$Type;
}}
declare module "packages/journeymap/client/ui/component/$ResetButton" {
import {$Button, $Button$Type} from "packages/journeymap/client/ui/component/$Button"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $ResetButton extends $Button {
readonly "category": $Category
static readonly "UNSET_ACTIVE_COLOR": integer
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

constructor(arg0: $Category$Type)
constructor(arg0: $Category$Type, arg1: $Button$OnPress$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResetButton$Type = ($ResetButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResetButton_ = $ResetButton$Type;
}}
declare module "packages/journeymap/client/data/$WorldData$DimensionProvider" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $WorldData$DimensionProvider {

 "getName"(): string
 "getDimensionId"(): string
 "getDimension"(): $ResourceKey<($Level)>
}

export namespace $WorldData$DimensionProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldData$DimensionProvider$Type = ($WorldData$DimensionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldData$DimensionProvider_ = $WorldData$DimensionProvider$Type;
}}
declare module "packages/journeymap/client/model/$NBTChunkMD" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockMD, $BlockMD$Type} from "packages/journeymap/client/model/$BlockMD"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $NBTChunkMD extends $ChunkMD {
static readonly "PROP_IS_SLIME_CHUNK": string
static readonly "PROP_LOADED": string
static readonly "PROP_LAST_RENDERED": string

constructor(arg0: $LevelChunk$Type, arg1: $ChunkPos$Type, arg2: $CompoundTag$Type, arg3: $MapType$Type)

public "getChunkBlockState"(arg0: $BlockPos$Type): $BlockState
public "getBlockMD"(arg0: $BlockPos$Type): $BlockMD
public "getBlockMD"(arg0: integer, arg1: integer, arg2: integer): $BlockMD
public "getSavedLightValue"(arg0: integer, arg1: integer, arg2: integer): integer
public "hasChunk"(): boolean
public "canBlockSeeTheSky"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getGetLightValue"(arg0: $BlockPos$Type): integer
public "fromNbt"(): boolean
public "getTopY"(arg0: $BlockPos$Type): integer
public "getBiome"(arg0: $BlockPos$Type): $Biome
public "getHeight"(arg0: $BlockPos$Type): integer
public "getPrecipitationHeight"(arg0: integer, arg1: integer): integer
public "getPrecipitationHeight"(arg0: $BlockPos$Type): integer
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "getChunk"(): $LevelChunk
get "chunk"(): $LevelChunk
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTChunkMD$Type = ($NBTChunkMD);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTChunkMD_ = $NBTChunkMD$Type;
}}
declare module "packages/journeymap/client/api/display/$ThemeButtonDisplay" {
import {$IThemeButton$Action, $IThemeButton$Action$Type} from "packages/journeymap/client/api/display/$IThemeButton$Action"
import {$IThemeButton, $IThemeButton$Type} from "packages/journeymap/client/api/display/$IThemeButton"

export interface $ThemeButtonDisplay {

 "addThemeButton"(arg0: string, arg1: string, arg2: $IThemeButton$Action$Type): $IThemeButton
 "addThemeButton"(arg0: string, arg1: string, arg2: string, arg3: $IThemeButton$Action$Type): $IThemeButton
 "addThemeToggleButton"(arg0: string, arg1: string, arg2: string, arg3: boolean, arg4: $IThemeButton$Action$Type): $IThemeButton
 "addThemeToggleButton"(arg0: string, arg1: string, arg2: boolean, arg3: $IThemeButton$Action$Type): $IThemeButton
}

export namespace $ThemeButtonDisplay {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeButtonDisplay$Type = ($ThemeButtonDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeButtonDisplay_ = $ThemeButtonDisplay$Type;
}}
declare module "packages/journeymap/client/render/draw/$DrawUtil$HAlign" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DrawUtil$HAlign extends $Enum<($DrawUtil$HAlign)> {
static readonly "Left": $DrawUtil$HAlign
static readonly "Center": $DrawUtil$HAlign
static readonly "Right": $DrawUtil$HAlign


public static "values"(): ($DrawUtil$HAlign)[]
public static "valueOf"(arg0: string): $DrawUtil$HAlign
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawUtil$HAlign$Type = (("left") | ("center") | ("right")) | ($DrawUtil$HAlign);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawUtil$HAlign_ = $DrawUtil$HAlign$Type;
}}
declare module "packages/journeymap/client/ui/component/$DraggableListPane" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$ScrollListPane, $ScrollListPane$Type} from "packages/journeymap/client/ui/component/$ScrollListPane"
import {$JmUI, $JmUI$Type} from "packages/journeymap/client/ui/component/$JmUI"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$SlotMetadata, $SlotMetadata$Type} from "packages/journeymap/client/ui/option/$SlotMetadata"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Slot, $Slot$Type} from "packages/journeymap/client/ui/component/$Slot"

export class $DraggableListPane<T extends $Slot> extends $ScrollListPane<(T)> {
 "lastTooltipMetadata": $SlotMetadata<(any)>
 "lastTooltip": $List<($FormattedCharSequence)>
 "lastTooltipTime": long
 "hoverDelay": long
 "scrolling": boolean
 "hovered": E

constructor(arg0: $JmUI$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer)

public "updatePosition"(arg0: integer, arg1: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "setSlots"(arg0: $List$Type<(T)>): void
public "updateSize"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
set "slots"(value: $List$Type<(T)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DraggableListPane$Type<T> = ($DraggableListPane<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DraggableListPane_<T> = $DraggableListPane$Type<(T)>;
}}
