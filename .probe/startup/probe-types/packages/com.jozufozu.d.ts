declare module "packages/com/jozufozu/flywheel/backend/instancing/entity/$EntityInstancingController" {
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$EntityInstance, $EntityInstance$Type} from "packages/com/jozufozu/flywheel/backend/instancing/entity/$EntityInstance"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityInstancingController<T extends $Entity> {

 "createInstance"(arg0: $MaterialManager$Type, arg1: T): $EntityInstance<(any)>
 "shouldSkipRender"(arg0: T): boolean
}

export namespace $EntityInstancingController {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityInstancingController$Type<T> = ($EntityInstancingController<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityInstancingController_<T> = $EntityInstancingController$Type<(T)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancedMaterial" {
import {$Material, $Material$Type} from "packages/com/jozufozu/flywheel/api/$Material"
import {$Instancer, $Instancer$Type} from "packages/com/jozufozu/flywheel/api/$Instancer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$GPUInstancer, $GPUInstancer$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$GPUInstancer"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Instanced, $Instanced$Type} from "packages/com/jozufozu/flywheel/api/struct/$Instanced"
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$PartialModel, $PartialModel$Type} from "packages/com/jozufozu/flywheel/core/$PartialModel"
import {$Model, $Model$Type} from "packages/com/jozufozu/flywheel/core/model/$Model"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $InstancedMaterial<D extends $InstanceData> implements $Material<(D)> {

constructor(arg0: $Instanced$Type<(D)>)

public "clear"(): void
public "delete"(): void
public "model"(arg0: any, arg1: $Supplier$Type<($Model$Type)>): $Instancer<(D)>
public "nothingToRender"(): boolean
public "getAllInstancers"(): $Collection<($GPUInstancer<(D)>)>
public "getInstanceCount"(): integer
public "getVertexCount"(): integer
public "getModel"(arg0: $BlockState$Type): $Instancer<(D)>
public "getModel"(arg0: $PartialModel$Type, arg1: $BlockState$Type, arg2: $Direction$Type, arg3: $Supplier$Type<($PoseStack$Type)>): $Instancer<(D)>
public "getModel"(arg0: $PartialModel$Type, arg1: $BlockState$Type, arg2: $Direction$Type): $Instancer<(D)>
public "getModel"(arg0: $PartialModel$Type): $Instancer<(D)>
public "getModel"(arg0: $PartialModel$Type, arg1: $BlockState$Type): $Instancer<(D)>
get "allInstancers"(): $Collection<($GPUInstancer<(D)>)>
get "instanceCount"(): integer
get "vertexCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancedMaterial$Type<D> = ($InstancedMaterial<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancedMaterial_<D> = $InstancedMaterial$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine" {
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$WorldProgram, $WorldProgram$Type} from "packages/com/jozufozu/flywheel/core/shader/$WorldProgram"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$ProgramCompiler, $ProgramCompiler$Type} from "packages/com/jozufozu/flywheel/core/compile/$ProgramCompiler"
import {$TaskEngine, $TaskEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$TaskEngine"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$RenderLayer, $RenderLayer$Type} from "packages/com/jozufozu/flywheel/backend/$RenderLayer"
import {$Engine, $Engine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$Engine"
import {$RenderLayerEvent, $RenderLayerEvent$Type} from "packages/com/jozufozu/flywheel/event/$RenderLayerEvent"
import {$InstancingEngine$OriginShiftListener, $InstancingEngine$OriginShiftListener$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$OriginShiftListener"
import {$InstancingEngine$GroupFactory, $InstancingEngine$GroupFactory$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$GroupFactory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InstancingEngine$Builder, $InstancingEngine$Builder$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$Builder"
import {$MaterialGroup, $MaterialGroup$Type} from "packages/com/jozufozu/flywheel/api/$MaterialGroup"

export class $InstancingEngine<P extends $WorldProgram> implements $Engine {
static "MAX_ORIGIN_DISTANCE": integer

constructor(arg0: $ProgramCompiler$Type<(P)>, arg1: $InstancingEngine$GroupFactory$Type<(P)>, arg2: boolean)

public static "builder"<P extends $WorldProgram>(arg0: $ProgramCompiler$Type<(P)>): $InstancingEngine$Builder<(P)>
public "delete"(): void
public "state"(arg0: $RenderLayer$Type, arg1: $RenderType$Type): $MaterialGroup
public "render"(arg0: $TaskEngine$Type, arg1: $RenderLayerEvent$Type): void
public "getOriginCoordinate"(): $Vec3i
public "addDebugInfo"(arg0: $List$Type<(string)>): void
public "addListener"(arg0: $InstancingEngine$OriginShiftListener$Type): void
public "beginFrame"(arg0: $Camera$Type): void
public "defaultTransparent"(): $MaterialGroup
public "defaultSolid"(): $MaterialGroup
public "solid"(arg0: $RenderType$Type): $MaterialGroup
public "transparent"(arg0: $RenderType$Type): $MaterialGroup
public "cutout"(arg0: $RenderType$Type): $MaterialGroup
public "defaultCutout"(): $MaterialGroup
get "originCoordinate"(): $Vec3i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancingEngine$Type<P> = ($InstancingEngine<(P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancingEngine_<P> = $InstancingEngine$Type<(P)>;
}}
declare module "packages/com/jozufozu/flywheel/event/$RenderLayerEvent" {
import {$RenderLayer, $RenderLayer$Type} from "packages/com/jozufozu/flywheel/backend/$RenderLayer"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$RenderBuffers, $RenderBuffers$Type} from "packages/net/minecraft/client/renderer/$RenderBuffers"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $RenderLayerEvent extends $Event {
readonly "type": $RenderType
readonly "stack": $PoseStack
readonly "viewProjection": $Matrix4f
readonly "buffers": $RenderBuffers
readonly "camX": double
readonly "camY": double
readonly "camZ": double
readonly "layer": $RenderLayer

constructor()
constructor(arg0: $ClientLevel$Type, arg1: $RenderType$Type, arg2: $PoseStack$Type, arg3: $RenderBuffers$Type, arg4: double, arg5: double, arg6: double)

public "toString"(): string
public "getLayer"(): $RenderLayer
public "getType"(): $RenderType
public "isCancelable"(): boolean
public "getCamX"(): double
public "getViewProjection"(): $Matrix4f
public "getCamY"(): double
public "getCamZ"(): double
public "getWorld"(): $ClientLevel
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "layer"(): $RenderLayer
get "type"(): $RenderType
get "cancelable"(): boolean
get "camX"(): double
get "viewProjection"(): $Matrix4f
get "camY"(): double
get "camZ"(): double
get "world"(): $ClientLevel
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLayerEvent$Type = ($RenderLayerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLayerEvent_ = $RenderLayerEvent$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$VertexCompiler" {
import {$VertexCompiler$Context, $VertexCompiler$Context$Type} from "packages/com/jozufozu/flywheel/core/compile/$VertexCompiler$Context"
import {$Template, $Template$Type} from "packages/com/jozufozu/flywheel/core/compile/$Template"
import {$GlShader, $GlShader$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$GlShader"
import {$Memoizer, $Memoizer$Type} from "packages/com/jozufozu/flywheel/core/compile/$Memoizer"
import {$FileResolution, $FileResolution$Type} from "packages/com/jozufozu/flywheel/core/source/$FileResolution"

export class $VertexCompiler extends $Memoizer<($VertexCompiler$Context), ($GlShader)> {

constructor(arg0: $Template$Type<(any)>, arg1: $FileResolution$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexCompiler$Type = ($VertexCompiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexCompiler_ = $VertexCompiler$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/vertex/$VertexWriter" {
import {$VertexList, $VertexList$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexList"

export interface $VertexWriter {

 "writeVertexList"(arg0: $VertexList$Type): void
 "writeVertex"(arg0: $VertexList$Type, arg1: integer): void
 "seekToVertex"(arg0: integer): void
 "intoReader"(): $VertexList
}

export namespace $VertexWriter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexWriter$Type = ($VertexWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexWriter_ = $VertexWriter$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/layout/$BufferLayout$Builder" {
import {$LayoutItem, $LayoutItem$Type} from "packages/com/jozufozu/flywheel/core/layout/$LayoutItem"
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export class $BufferLayout$Builder {

constructor()

public "build"(): $BufferLayout
public "addItems"(...arg0: ($LayoutItem$Type)[]): $BufferLayout$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferLayout$Builder$Type = ($BufferLayout$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferLayout$Builder_ = $BufferLayout$Builder$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/materials/$BasicWriterUnsafe" {
import {$StructType, $StructType$Type} from "packages/com/jozufozu/flywheel/api/struct/$StructType"
import {$BasicData, $BasicData$Type} from "packages/com/jozufozu/flywheel/core/materials/$BasicData"
import {$UnsafeBufferWriter, $UnsafeBufferWriter$Type} from "packages/com/jozufozu/flywheel/backend/struct/$UnsafeBufferWriter"
import {$VecBuffer, $VecBuffer$Type} from "packages/com/jozufozu/flywheel/backend/gl/buffer/$VecBuffer"

export class $BasicWriterUnsafe<D extends $BasicData> extends $UnsafeBufferWriter<(D)> {

constructor(arg0: $VecBuffer$Type, arg1: $StructType$Type<(D)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicWriterUnsafe$Type<D> = ($BasicWriterUnsafe<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicWriterUnsafe_<D> = $BasicWriterUnsafe$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$RenderTypeExtension" {
import {$DrawBuffer, $DrawBuffer$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$DrawBuffer"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $RenderTypeExtension {

 "flywheel$getDrawBuffer"(): $DrawBuffer

(): $DrawBuffer
}

export namespace $RenderTypeExtension {
function getDrawBuffer(arg0: $RenderType$Type): $DrawBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypeExtension$Type = ($RenderTypeExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypeExtension_ = $RenderTypeExtension$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/blockentity/$BlockEntityInstanceManager" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$InstanceManager, $InstanceManager$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$InstanceManager"

export class $BlockEntityInstanceManager extends $InstanceManager<($BlockEntity)> {
readonly "materialManager": $MaterialManager

constructor(arg0: $MaterialManager$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityInstanceManager$Type = ($BlockEntityInstanceManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityInstanceManager_ = $BlockEntityInstanceManager$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/box/$CoordinateConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CoordinateConsumer {

 "consume"(arg0: integer, arg1: integer, arg2: integer): void

(arg0: integer, arg1: integer, arg2: integer): void
}

export namespace $CoordinateConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoordinateConsumer$Type = ($CoordinateConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoordinateConsumer_ = $CoordinateConsumer$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/$Color" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $Color {
static readonly "TRANSPARENT_BLACK": $Color
static readonly "BLACK": $Color
static readonly "WHITE": $Color
static readonly "RED": $Color
static readonly "GREEN": $Color
static readonly "SPRING_GREEN": $Color

constructor(arg0: integer, arg1: boolean)
constructor(arg0: integer)
constructor(arg0: float, arg1: float, arg2: float, arg3: float)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: integer)

public "setValue"(arg0: integer): $Color
public "copy"(): $Color
public "copy"(arg0: boolean): $Color
public "asVectorF"(): $Vector3f
public "setBlue"(arg0: float): $Color
public "setBlue"(arg0: integer): $Color
public "mixWith"(arg0: $Color$Type, arg1: float): $Color
public "getAlphaAsFloat"(): float
public "asVector"(): $Vec3
public "scaleAlpha"(arg0: float): $Color
public "modifyValue"(arg0: $UnaryOperator$Type<(integer)>): $Color
public static "rainbowColor"(arg0: integer): $Color
public static "generateFromLong"(arg0: long): $Color
public "getRGB"(): integer
public "getRed"(): integer
public "getGreen"(): integer
public "getBlue"(): integer
public "getAlpha"(): integer
public "brighter"(): $Color
public "darker"(): $Color
public "setAlpha"(arg0: float): $Color
public "setAlpha"(arg0: integer): $Color
public "setImmutable"(): $Color
public "getGreenAsFloat"(): float
public "getBlueAsFloat"(): float
public "getRedAsFloat"(): float
public static "mixColors"(arg0: $Color$Type, arg1: $Color$Type, arg2: float): $Color
public static "mixColors"(arg0: integer, arg1: integer, arg2: float): integer
public "setGreen"(arg0: integer): $Color
public "setGreen"(arg0: float): $Color
public "setRed"(arg0: integer): $Color
public "setRed"(arg0: float): $Color
set "value"(value: integer)
set "blue"(value: float)
set "blue"(value: integer)
get "alphaAsFloat"(): float
get "rGB"(): integer
get "red"(): integer
get "green"(): integer
get "blue"(): integer
get "alpha"(): integer
set "alpha"(value: float)
set "alpha"(value: integer)
get "greenAsFloat"(): float
get "blueAsFloat"(): float
get "redAsFloat"(): float
set "green"(value: integer)
set "green"(value: float)
set "red"(value: integer)
set "red"(value: float)
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
declare module "packages/com/jozufozu/flywheel/core/source/$SourceFinder" {
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $SourceFinder {

 "findSource"(arg0: $ResourceLocation$Type): $SourceFile

(arg0: $ResourceLocation$Type): $SourceFile
}

export namespace $SourceFinder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SourceFinder$Type = ($SourceFinder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SourceFinder_ = $SourceFinder$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/struct/$UnsafeBufferWriter" {
import {$BufferWriter, $BufferWriter$Type} from "packages/com/jozufozu/flywheel/backend/struct/$BufferWriter"

export class $UnsafeBufferWriter<S> extends $BufferWriter<(S)> {


public "seek"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnsafeBufferWriter$Type<S> = ($UnsafeBufferWriter<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnsafeBufferWriter_<S> = $UnsafeBufferWriter$Type<(S)>;
}}
declare module "packages/com/jozufozu/flywheel/event/$BeginFrameEvent" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"

export class $BeginFrameEvent extends $Event {

constructor()
constructor(arg0: $ClientLevel$Type, arg1: $Camera$Type, arg2: $Frustum$Type)

public "getCameraPos"(): $Vec3
public "isCancelable"(): boolean
public "getWorld"(): $ClientLevel
public "getFrustum"(): $Frustum
public "getCamera"(): $Camera
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cameraPos"(): $Vec3
get "cancelable"(): boolean
get "world"(): $ClientLevel
get "frustum"(): $Frustum
get "camera"(): $Camera
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeginFrameEvent$Type = ($BeginFrameEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeginFrameEvent_ = $BeginFrameEvent$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$AbstractInstancer" {
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$Instancer, $Instancer$Type} from "packages/com/jozufozu/flywheel/api/$Instancer"

export class $AbstractInstancer<D extends $InstanceData> implements $Instancer<(D)> {


public "toString"(): string
public "clear"(): void
public "createInstance"(): D
public "notifyRemoval"(): void
public "stealInstance"(arg0: D): void
public "getModelVertexCount"(): integer
public "getInstanceCount"(): integer
public "getVertexCount"(): integer
public "createInstances"(arg0: (D)[]): void
public "notifyDirty"(): void
get "modelVertexCount"(): integer
get "instanceCount"(): integer
get "vertexCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractInstancer$Type<D> = ($AbstractInstancer<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractInstancer_<D> = $AbstractInstancer$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/$GLSLVersion" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GLSLVersion extends $Enum<($GLSLVersion)> {
static readonly "V150": $GLSLVersion
static readonly "V330": $GLSLVersion
readonly "version": integer


public "toString"(): string
public static "values"(): ($GLSLVersion)[]
public static "valueOf"(arg0: string): $GLSLVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLSLVersion$Type = (("v150") | ("v330")) | ($GLSLVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLSLVersion_ = $GLSLVersion$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/model/$ShadeSeparatedBufferedData" {
import {$BufferBuilder$DrawState, $BufferBuilder$DrawState$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder$DrawState"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $ShadeSeparatedBufferedData {

 "release"(): void
 "drawState"(): $BufferBuilder$DrawState
 "indexBuffer"(): $ByteBuffer
 "vertexBuffer"(): $ByteBuffer
 "unshadedStartVertex"(): integer
}

export namespace $ShadeSeparatedBufferedData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShadeSeparatedBufferedData$Type = ($ShadeSeparatedBufferedData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShadeSeparatedBufferedData_ = $ShadeSeparatedBufferedData$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/instance/$DynamicInstance" {
import {$Instance, $Instance$Type} from "packages/com/jozufozu/flywheel/api/instance/$Instance"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $DynamicInstance extends $Instance {

 "decreaseFramerateWithDistance"(): boolean
 "beginFrame"(): void
 "getWorldPosition"(): $BlockPos
}

export namespace $DynamicInstance {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicInstance$Type = ($DynamicInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicInstance_ = $DynamicInstance$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$ShaderStruct" {
import {$StructField, $StructField$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$StructField"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$AbstractShaderElement, $AbstractShaderElement$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$AbstractShaderElement"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $ShaderStruct extends $AbstractShaderElement {
static readonly "struct": $Pattern
readonly "name": $Span
readonly "body": $Span
readonly "self": $Span

constructor(arg0: $Span$Type, arg1: $Span$Type, arg2: $Span$Type)

public "getName"(): $Span
public "toString"(): string
public "getFields"(): $ImmutableList<($StructField)>
public "getBody"(): $Span
get "name"(): $Span
get "fields"(): $ImmutableList<($StructField)>
get "body"(): $Span
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderStruct$Type = ($ShaderStruct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderStruct_ = $ShaderStruct$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/$MaterialGroup" {
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$StructType, $StructType$Type} from "packages/com/jozufozu/flywheel/api/struct/$StructType"
import {$Material, $Material$Type} from "packages/com/jozufozu/flywheel/api/$Material"

export interface $MaterialGroup {

 "material"<D extends $InstanceData>(arg0: $StructType$Type<(D)>): $Material<(D)>

(arg0: $StructType$Type<(D)>): $Material<(D)>
}

export namespace $MaterialGroup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaterialGroup$Type = ($MaterialGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaterialGroup_ = $MaterialGroup$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/blockentity/$BlockEntityInstancingController" {
import {$BlockEntityInstance, $BlockEntityInstance$Type} from "packages/com/jozufozu/flywheel/backend/instancing/blockentity/$BlockEntityInstance"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"

export interface $BlockEntityInstancingController<T extends $BlockEntity> {

 "createInstance"(arg0: $MaterialManager$Type, arg1: T): $BlockEntityInstance<(any)>
 "shouldSkipRender"(arg0: T): boolean
}

export namespace $BlockEntityInstancingController {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityInstancingController$Type<T> = ($BlockEntityInstancingController<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityInstancingController_<T> = $BlockEntityInstancingController$Type<(T)>;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$Variable$Qualifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $Variable$Qualifier extends $Enum<($Variable$Qualifier)> {
static readonly "NONE": $Variable$Qualifier
static readonly "IN": $Variable$Qualifier
static readonly "OUT": $Variable$Qualifier
static readonly "INOUT": $Variable$Qualifier
static readonly "ERROR": $Variable$Qualifier


public static "values"(): ($Variable$Qualifier)[]
public static "valueOf"(arg0: string): $Variable$Qualifier
public static "fromSpan"(arg0: $Span$Type): $Variable$Qualifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Variable$Qualifier$Type = (("inout") | ("in") | ("none") | ("error") | ("out")) | ($Variable$Qualifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Variable$Qualifier_ = $Variable$Qualifier$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$Import" {
import {$Resolver, $Resolver$Type} from "packages/com/jozufozu/flywheel/core/source/$Resolver"
import {$AbstractShaderElement, $AbstractShaderElement$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$AbstractShaderElement"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$FileResolution, $FileResolution$Type} from "packages/com/jozufozu/flywheel/core/source/$FileResolution"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $Import extends $AbstractShaderElement {
static readonly "IMPORTS": $List<($Import)>
readonly "self": $Span

constructor(arg0: $Resolver$Type, arg1: $Span$Type, arg2: $Span$Type)

public "getFile"(): $SourceFile
public "getOptional"(): $Optional<($SourceFile)>
public "getFileLoc"(): $ResourceLocation
public "getResolution"(): $FileResolution
get "file"(): $SourceFile
get "optional"(): $Optional<($SourceFile)>
get "fileLoc"(): $ResourceLocation
get "resolution"(): $FileResolution
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Import$Type = ($Import);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Import_ = $Import$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/model/$ElementBuffer" {
import {$VertexFormat$IndexType, $VertexFormat$IndexType$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$IndexType"

export class $ElementBuffer {

constructor(arg0: integer, arg1: integer, arg2: $VertexFormat$IndexType$Type)

public "bind"(): void
public "getEboIndexType"(): $VertexFormat$IndexType
public "getElementCount"(): integer
get "eboIndexType"(): $VertexFormat$IndexType
get "elementCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementBuffer$Type = ($ElementBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementBuffer_ = $ElementBuffer$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/$MaterialManager" {
import {$RenderLayer, $RenderLayer$Type} from "packages/com/jozufozu/flywheel/backend/$RenderLayer"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$MaterialGroup, $MaterialGroup$Type} from "packages/com/jozufozu/flywheel/api/$MaterialGroup"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $MaterialManager {

 "state"(arg0: $RenderLayer$Type, arg1: $RenderType$Type): $MaterialGroup
 "defaultTransparent"(): $MaterialGroup
 "defaultSolid"(): $MaterialGroup
 "getOriginCoordinate"(): $Vec3i
 "solid"(arg0: $RenderType$Type): $MaterialGroup
 "transparent"(arg0: $RenderType$Type): $MaterialGroup
 "cutout"(arg0: $RenderType$Type): $MaterialGroup
 "defaultCutout"(): $MaterialGroup
}

export namespace $MaterialManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaterialManager$Type = ($MaterialManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaterialManager_ = $MaterialManager$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$Engine" {
import {$RenderLayer, $RenderLayer$Type} from "packages/com/jozufozu/flywheel/backend/$RenderLayer"
import {$RenderLayerEvent, $RenderLayerEvent$Type} from "packages/com/jozufozu/flywheel/event/$RenderLayerEvent"
import {$RenderDispatcher, $RenderDispatcher$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$RenderDispatcher"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$MaterialGroup, $MaterialGroup$Type} from "packages/com/jozufozu/flywheel/api/$MaterialGroup"
import {$TaskEngine, $TaskEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$TaskEngine"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $Engine extends $RenderDispatcher, $MaterialManager {

 "addDebugInfo"(arg0: $List$Type<(string)>): void
 "delete"(): void
 "render"(arg0: $TaskEngine$Type, arg1: $RenderLayerEvent$Type): void
 "beginFrame"(arg0: $Camera$Type): void
 "state"(arg0: $RenderLayer$Type, arg1: $RenderType$Type): $MaterialGroup
 "defaultTransparent"(): $MaterialGroup
 "defaultSolid"(): $MaterialGroup
 "getOriginCoordinate"(): $Vec3i
 "solid"(arg0: $RenderType$Type): $MaterialGroup
 "transparent"(arg0: $RenderType$Type): $MaterialGroup
 "cutout"(arg0: $RenderType$Type): $MaterialGroup
 "defaultCutout"(): $MaterialGroup
}

export namespace $Engine {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Engine$Type = ($Engine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Engine_ = $Engine$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/virtual/$VirtualRenderWorld" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$LevelLightEngine, $LevelLightEngine$Type} from "packages/net/minecraft/world/level/lighting/$LevelLightEngine"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$FlywheelWorld, $FlywheelWorld$Type} from "packages/com/jozufozu/flywheel/api/$FlywheelWorld"
import {$LevelTickAccess, $LevelTickAccess$Type} from "packages/net/minecraft/world/ticks/$LevelTickAccess"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$SoundSource, $SoundSource$Type} from "packages/net/minecraft/sounds/$SoundSource"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$TickingBlockEntity, $TickingBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TickingBlockEntity"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MapItemSavedData, $MapItemSavedData$Type} from "packages/net/minecraft/world/level/saveddata/maps/$MapItemSavedData"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$GameEvent$Context, $GameEvent$Context$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent$Context"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$BlockSnapshot, $BlockSnapshot$Type} from "packages/net/minecraftforge/common/util/$BlockSnapshot"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ChunkSource, $ChunkSource$Type} from "packages/net/minecraft/world/level/chunk/$ChunkSource"
import {$Scoreboard, $Scoreboard$Type} from "packages/net/minecraft/world/scores/$Scoreboard"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BiomeManager, $BiomeManager$Type} from "packages/net/minecraft/world/level/biome/$BiomeManager"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$RecipeManager, $RecipeManager$Type} from "packages/net/minecraft/world/item/crafting/$RecipeManager"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $VirtualRenderWorld extends $Level implements $FlywheelWorld {
static readonly "RESOURCE_KEY_CODEC": $Codec<($ResourceKey<($Level)>)>
static readonly "OVERWORLD": $ResourceKey<($Level)>
static readonly "NETHER": $ResourceKey<($Level)>
static readonly "END": $ResourceKey<($Level)>
static readonly "MAX_LEVEL_SIZE": integer
static readonly "LONG_PARTICLE_CLIP_RANGE": integer
static readonly "SHORT_PARTICLE_CLIP_RANGE": integer
static readonly "MAX_BRIGHTNESS": integer
static readonly "TICKS_PER_DAY": integer
static readonly "MAX_ENTITY_SPAWN_Y": integer
static readonly "MIN_ENTITY_SPAWN_Y": integer
readonly "blockEntityTickers": $List<($TickingBlockEntity)>
 "oRainLevel": float
 "rainLevel": float
 "oThunderLevel": float
 "thunderLevel": float
readonly "random": $RandomSource
readonly "isClientSide": boolean
 "restoringBlockSnapshots": boolean
 "captureBlockSnapshots": boolean
 "capturedBlockSnapshots": $ArrayList<($BlockSnapshot)>

constructor(arg0: $Level$Type, arg1: integer, arg2: integer, arg3: $Vec3i$Type)
constructor(arg0: $Level$Type, arg1: $Vec3i$Type)
constructor(arg0: $Level$Type)

public "clear"(): void
public "getBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getBiome"(arg0: $BlockPos$Type): $Holder<($Biome)>
public "getChunkSource"(): $ChunkSource
public "getScoreboard"(): $Scoreboard
public "getFluidState"(arg0: $BlockPos$Type): $FluidState
public "getMaxLocalRawBrightness"(arg0: $BlockPos$Type): integer
public "getLightEngine"(): $LevelLightEngine
public "playSeededSound"(arg0: $Player$Type, arg1: double, arg2: double, arg3: double, arg4: $Holder$Type<($SoundEvent$Type)>, arg5: $SoundSource$Type, arg6: float, arg7: float, arg8: long): void
public "playSeededSound"(arg0: $Player$Type, arg1: $Entity$Type, arg2: $Holder$Type<($SoundEvent$Type)>, arg3: $SoundSource$Type, arg4: float, arg5: float, arg6: long): void
public "removeBlockEntity"(arg0: $BlockPos$Type): void
public "setBlockEntity"(arg0: $BlockEntity$Type): void
public "gatherChunkSourceStats"(): string
public "getEntity"(arg0: integer): $Entity
public "getMapData"(arg0: string): $MapItemSavedData
public "setMapData"(arg0: string, arg1: $MapItemSavedData$Type): void
public "getFreeMapId"(): integer
public "destroyBlockProgress"(arg0: integer, arg1: $BlockPos$Type, arg2: integer): void
public "getRecipeManager"(): $RecipeManager
public "getSectionsCount"(): integer
public "isOutsideBuildHeight"(arg0: integer): boolean
public "getMinSection"(): integer
public "getMaxSection"(): integer
public "getChunk"(arg0: $BlockPos$Type): $ChunkAccess
public "getSectionIndexFromSectionY"(arg0: integer): integer
public "getSectionYFromSectionIndex"(arg0: integer): integer
public "getSectionIndex"(arg0: integer): integer
public "getHeight"(): integer
public "getBlockTicks"(): $LevelTickAccess<($Block)>
public "getFluidTicks"(): $LevelTickAccess<($Fluid)>
public "getUncachedNoiseBiome"(arg0: integer, arg1: integer, arg2: integer): $Holder<($Biome)>
public "getNoiseBiome"(arg0: integer, arg1: integer, arg2: integer): $Holder<($Biome)>
public "isAreaLoaded"(arg0: $BlockPos$Type, arg1: integer): boolean
public "getShade"(arg0: $Direction$Type, arg1: boolean): float
public "levelEvent"(arg0: $Player$Type, arg1: integer, arg2: $BlockPos$Type, arg3: integer): void
public "gameEvent"(arg0: $GameEvent$Type, arg1: $Vec3$Type, arg2: $GameEvent$Context$Type): void
public "isLoaded"(arg0: $BlockPos$Type): boolean
public "getMinBuildHeight"(): integer
public "players"(): $List<(any)>
public "enabledFeatures"(): $FeatureFlagSet
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "setBlock"(arg0: $BlockPos$Type, arg1: $BlockState$Type, arg2: integer, arg3: integer): boolean
public "isOutsideBuildHeight"(arg0: $BlockPos$Type): boolean
public "updateNeighbourForOutputSignal"(arg0: $BlockPos$Type, arg1: $Block$Type): void
public "sendBlockUpdated"(arg0: $BlockPos$Type, arg1: $BlockState$Type, arg2: $BlockState$Type, arg3: integer): void
public "getBlockState"(arg0: integer, arg1: integer, arg2: integer): $BlockState
public "getAnyChunkImmediately"(arg0: integer, arg1: integer): $ChunkAccess
public "getChunkAtImmediately"(arg0: integer, arg1: integer): $LevelChunk
public "getChunk"(arg0: integer, arg1: integer): $LevelChunk
public "getMaxBuildHeight"(): integer
public "getBiomeManager"(): $BiomeManager
public "setBlockEntities"(arg0: $Collection$Type<($BlockEntity$Type)>): void
public static "nextMultipleOf16"(arg0: integer): integer
public "runLightEngine"(): void
public "actuallyGetChunk"(arg0: integer, arg1: integer): $ChunkAccess
public "supportsFlywheel"(): boolean
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
get "chunkSource"(): $ChunkSource
get "scoreboard"(): $Scoreboard
get "lightEngine"(): $LevelLightEngine
set "blockEntity"(value: $BlockEntity$Type)
get "freeMapId"(): integer
get "recipeManager"(): $RecipeManager
get "sectionsCount"(): integer
get "minSection"(): integer
get "maxSection"(): integer
get "height"(): integer
get "blockTicks"(): $LevelTickAccess<($Block)>
get "fluidTicks"(): $LevelTickAccess<($Fluid)>
get "minBuildHeight"(): integer
get "maxBuildHeight"(): integer
get "biomeManager"(): $BiomeManager
set "blockEntities"(value: $Collection$Type<($BlockEntity$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VirtualRenderWorld$Type = ($VirtualRenderWorld);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VirtualRenderWorld_ = $VirtualRenderWorld$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/box/$ImmutableBox" {
import {$GridAlignedBB, $GridAlignedBB$Type} from "packages/com/jozufozu/flywheel/util/box/$GridAlignedBB"
import {$CoordinateConsumer, $CoordinateConsumer$Type} from "packages/com/jozufozu/flywheel/util/box/$CoordinateConsumer"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export interface $ImmutableBox {

 "contains"(arg0: $ImmutableBox$Type): boolean
 "contains"(arg0: integer, arg1: integer, arg2: integer): boolean
 "empty"(): boolean
 "copy"(): $GridAlignedBB
 "union"(arg0: $ImmutableBox$Type): $ImmutableBox
 "intersects"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
 "intersects"(arg0: $ImmutableBox$Type): boolean
 "getMaxX"(): integer
 "getMinZ"(): integer
 "getMaxZ"(): integer
 "getMinX"(): integer
 "getMaxY"(): integer
 "getMinY"(): integer
 "isContainedBy"(arg0: $GridAlignedBB$Type): boolean
 "toAABB"(): $AABB
 "sameAs"(arg0: $ImmutableBox$Type, arg1: integer): boolean
 "sameAs"(arg0: $AABB$Type): boolean
 "sameAs"(arg0: $ImmutableBox$Type): boolean
 "hasPowerOf2Sides"(): boolean
 "forEachContained"(arg0: $CoordinateConsumer$Type): void
 "volume"(): integer
 "intersect"(arg0: $ImmutableBox$Type): $GridAlignedBB
 "sizeY"(): integer
 "sizeX"(): integer
 "sizeZ"(): integer
}

export namespace $ImmutableBox {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmutableBox$Type = ($ImmutableBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmutableBox_ = $ImmutableBox$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/model/$BufferBuilderExtension" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export interface $BufferBuilderExtension {

 "flywheel$injectForRender"(arg0: $ByteBuffer$Type, arg1: $VertexFormat$Type, arg2: integer): void
 "flywheel$freeBuffer"(): void
 "flywheel$appendBufferUnsafe"(arg0: $ByteBuffer$Type): void
 "flywheel$getVertices"(): integer
}

export namespace $BufferBuilderExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferBuilderExtension$Type = ($BufferBuilderExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferBuilderExtension_ = $BufferBuilderExtension$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/$ShaderSources" {
import {$Index, $Index$Type} from "packages/com/jozufozu/flywheel/core/source/$Index"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SourceFinder, $SourceFinder$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFinder"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $ShaderSources implements $SourceFinder {
static readonly "SHADER_DIR": string
static readonly "SHADER_DIR_SLASH": string
static readonly "EXTENSIONS": $ArrayList<(string)>
readonly "index": $Index

constructor(arg0: $ResourceManager$Type)

public "findSource"(arg0: $ResourceLocation$Type): $SourceFile
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderSources$Type = ($ShaderSources);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderSources_ = $ShaderSources$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/transform/$TransformStack" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$TStack, $TStack$Type} from "packages/com/jozufozu/flywheel/util/transform/$TStack"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Transform, $Transform$Type} from "packages/com/jozufozu/flywheel/util/transform/$Transform"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Axis, $Axis$Type} from "packages/com/mojang/math/$Axis"

export interface $TransformStack extends $Transform<($TransformStack)>, $TStack<($TransformStack)> {

 "transform"(arg0: $PoseStack$Type): $TransformStack
 "transform"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type): $TransformStack
 "mulPose"(arg0: $Matrix4f$Type): $TransformStack
 "rotateCentered"(arg0: $Direction$Type, arg1: float): $TransformStack
 "rotateCentered"(arg0: $Quaternionf$Type): $TransformStack
 "mulNormal"(arg0: $Matrix3f$Type): $TransformStack
 "pushPose"(): $TransformStack
 "popPose"(): $TransformStack
 "centre"(): $TransformStack
 "translate"(arg0: $Vec3i$Type): $TransformStack
 "translate"(arg0: $Vec3$Type): $TransformStack
 "translate"(arg0: $Vector3f$Type): $TransformStack
 "translate"(arg0: double, arg1: double, arg2: double): $TransformStack
 "translateZ"(arg0: double): $TransformStack
 "translateAll"(arg0: double): $TransformStack
 "unCentre"(): $TransformStack
 "translateX"(arg0: double): $TransformStack
 "translateY"(arg0: double): $TransformStack
 "nudge"(arg0: integer): $TransformStack
 "translateBack"(arg0: $Vec3$Type): $TransformStack
 "translateBack"(arg0: $Vec3i$Type): $TransformStack
 "translateBack"(arg0: double, arg1: double, arg2: double): $TransformStack
 "multiply"(arg0: $Axis$Type, arg1: double): $TransformStack
 "multiply"(arg0: $Vector3f$Type, arg1: double): $TransformStack
 "multiply"(arg0: $Quaternionf$Type): $TransformStack
 "rotate"(arg0: double, arg1: $Direction$Axis$Type): $TransformStack
 "rotate"(arg0: $Direction$Type, arg1: float): $TransformStack
 "rotateXRadians"(arg0: double): $TransformStack
 "rotateToFace"(arg0: $Direction$Type): $TransformStack
 "rotateYRadians"(arg0: double): $TransformStack
 "rotateZRadians"(arg0: double): $TransformStack
 "multiplyRadians"(arg0: $Axis$Type, arg1: double): $TransformStack
 "multiplyRadians"(arg0: $Vector3f$Type, arg1: double): $TransformStack
 "rotateZ"(arg0: double): $TransformStack
 "rotateX"(arg0: double): $TransformStack
 "rotateY"(arg0: double): $TransformStack
 "scale"(arg0: float, arg1: float, arg2: float): $TransformStack
 "scale"(arg0: float): $TransformStack
}

export namespace $TransformStack {
function cast(arg0: $PoseStack$Type): $TransformStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransformStack$Type = ($TransformStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransformStack_ = $TransformStack$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/materials/$BasicData" {
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$Color, $Color$Type} from "packages/com/jozufozu/flywheel/util/$Color"
import {$FlatLit, $FlatLit$Type} from "packages/com/jozufozu/flywheel/core/materials/$FlatLit"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $BasicData extends $InstanceData implements $FlatLit<($BasicData)> {
 "blockLight": byte
 "skyLight": byte
 "r": byte
 "g": byte
 "b": byte
 "a": byte

constructor()

public "setColor"(arg0: integer, arg1: integer, arg2: integer): $BasicData
public "setColor"(arg0: integer, arg1: boolean): $BasicData
public "setColor"(arg0: integer): $BasicData
public "setColor"(arg0: byte, arg1: byte, arg2: byte): $BasicData
public "setColor"(arg0: byte, arg1: byte, arg2: byte, arg3: byte): $BasicData
public "setColor"(arg0: $Color$Type): $BasicData
public "setSkyLight"(arg0: integer): $BasicData
public "getPackedLight"(): integer
public "updateLight"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type): $BasicData
set "color"(value: integer)
set "color"(value: $Color$Type)
set "skyLight"(value: integer)
get "packedLight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicData$Type = ($BasicData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicData_ = $BasicData$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/transform/$Translate" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export interface $Translate<Self> {

 "centre"(): Self
 "translate"(arg0: $Vec3i$Type): Self
 "translate"(arg0: $Vec3$Type): Self
 "translate"(arg0: $Vector3f$Type): Self
 "translate"(arg0: double, arg1: double, arg2: double): Self
 "translateZ"(arg0: double): Self
 "translateAll"(arg0: double): Self
 "unCentre"(): Self
 "translateX"(arg0: double): Self
 "translateY"(arg0: double): Self
 "nudge"(arg0: integer): Self
 "translateBack"(arg0: $Vec3$Type): Self
 "translateBack"(arg0: $Vec3i$Type): Self
 "translateBack"(arg0: double, arg1: double, arg2: double): Self

(): Self
}

export namespace $Translate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Translate$Type<Self> = ($Translate<(Self)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Translate_<Self> = $Translate$Type<(Self)>;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$FragmentData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FragmentData {

 "generateFooter"(): string

(): string
}

export namespace $FragmentData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FragmentData$Type = ($FragmentData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FragmentData_ = $FragmentData$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$GroupFactory" {
import {$WorldProgram, $WorldProgram$Type} from "packages/com/jozufozu/flywheel/core/shader/$WorldProgram"
import {$InstancedMaterialGroup, $InstancedMaterialGroup$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancedMaterialGroup"
import {$InstancingEngine, $InstancingEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $InstancingEngine$GroupFactory<P extends $WorldProgram> {

 "create"(arg0: $InstancingEngine$Type<(P)>, arg1: $RenderType$Type): $InstancedMaterialGroup<(P)>

(arg0: $InstancingEngine$Type<(P)>, arg1: $RenderType$Type): $InstancedMaterialGroup<(P)>
}

export namespace $InstancingEngine$GroupFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancingEngine$GroupFactory$Type<P> = ($InstancingEngine$GroupFactory<(P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancingEngine$GroupFactory_<P> = $InstancingEngine$GroupFactory$Type<(P)>;
}}
declare module "packages/com/jozufozu/flywheel/util/$ClientLevelExtension" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ClientLevelExtension {

 "flywheel$getAllLoadedEntities"(): $Iterable<($Entity)>

(arg0: $ClientLevel$Type): $ClientLevelExtension
}

export namespace $ClientLevelExtension {
function cast(arg0: $ClientLevel$Type): $ClientLevelExtension
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelExtension$Type = ($ClientLevelExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelExtension_ = $ClientLevelExtension$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/$InstanceData" {
import {$Instancer, $Instancer$Type} from "packages/com/jozufozu/flywheel/api/$Instancer"

export class $InstanceData {

constructor()

public "delete"(): void
public "getOwner"(): $Instancer<(any)>
public "setOwner"(arg0: $Instancer$Type<(any)>): $InstanceData
public "markDirty"(): void
public "isRemoved"(): boolean
public "checkDirtyAndClear"(): boolean
get "owner"(): $Instancer<(any)>
set "owner"(value: $Instancer$Type<(any)>)
get "removed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstanceData$Type = ($InstanceData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstanceData_ = $InstanceData$Type;
}}
declare module "packages/com/jozufozu/flywheel/light/$TickingLightListener" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$LightListener, $LightListener$Type} from "packages/com/jozufozu/flywheel/light/$LightListener"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export interface $TickingLightListener extends $LightListener {

 "tickLightListener"(): boolean
 "onLightUpdate"(arg0: $LightLayer$Type, arg1: $ImmutableBox$Type): void
 "isListenerInvalid"(): boolean
 "onLightPacket"(arg0: integer, arg1: integer): void
 "getVolume"(): $ImmutableBox
}

export namespace $TickingLightListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingLightListener$Type = ($TickingLightListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingLightListener_ = $TickingLightListener$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/struct/$Instanced" {
import {$StructType, $StructType$Type} from "packages/com/jozufozu/flywheel/api/struct/$StructType"
import {$StructWriter, $StructWriter$Type} from "packages/com/jozufozu/flywheel/api/struct/$StructWriter"
import {$VecBuffer, $VecBuffer$Type} from "packages/com/jozufozu/flywheel/backend/gl/buffer/$VecBuffer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export interface $Instanced<S> extends $StructType<(S)> {

 "getWriter"(arg0: $VecBuffer$Type): $StructWriter<(S)>
 "getProgramSpec"(): $ResourceLocation
 "create"(): S
 "getLayout"(): $BufferLayout
}

export namespace $Instanced {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instanced$Type<S> = ($Instanced<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instanced_<S> = $Instanced$Type<(S)>;
}}
declare module "packages/com/jozufozu/flywheel/light/$LightListener" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export interface $LightListener {

 "onLightUpdate"(arg0: $LightLayer$Type, arg1: $ImmutableBox$Type): void
 "isListenerInvalid"(): boolean
 "onLightPacket"(arg0: integer, arg1: integer): void
 "getVolume"(): $ImmutableBox
}

export namespace $LightListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightListener$Type = ($LightListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightListener_ = $LightListener$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$Builder" {
import {$InstancingEngine$GroupFactory, $InstancingEngine$GroupFactory$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$GroupFactory"
import {$WorldProgram, $WorldProgram$Type} from "packages/com/jozufozu/flywheel/core/shader/$WorldProgram"
import {$ProgramCompiler, $ProgramCompiler$Type} from "packages/com/jozufozu/flywheel/core/compile/$ProgramCompiler"
import {$InstancingEngine, $InstancingEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine"

export class $InstancingEngine$Builder<P extends $WorldProgram> {

constructor(arg0: $ProgramCompiler$Type<(P)>)

public "setIgnoreOriginCoordinate"(arg0: boolean): $InstancingEngine$Builder<(P)>
public "build"(): $InstancingEngine<(P)>
public "setGroupFactory"(arg0: $InstancingEngine$GroupFactory$Type<(P)>): $InstancingEngine$Builder<(P)>
set "ignoreOriginCoordinate"(value: boolean)
set "groupFactory"(value: $InstancingEngine$GroupFactory$Type<(P)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancingEngine$Builder$Type<P> = ($InstancingEngine$Builder<(P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancingEngine$Builder_<P> = $InstancingEngine$Builder$Type<(P)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/$GlVertexArray" {
import {$GlObject, $GlObject$Type} from "packages/com/jozufozu/flywheel/backend/gl/$GlObject"
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export class $GlVertexArray extends $GlObject {

constructor()

public "bindAttributes"(arg0: integer, arg1: $BufferLayout$Type): void
public "bind"(): void
public static "bind"(arg0: integer): void
public "enableArrays"(arg0: integer): void
public static "unbind"(): void
public "disableArrays"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexArray$Type = ($GlVertexArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexArray_ = $GlVertexArray$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/shader/$GameStateProvider" {
import {$ShaderConstants, $ShaderConstants$Type} from "packages/com/jozufozu/flywheel/core/shader/$ShaderConstants"

export interface $GameStateProvider {

 "isTrue"(): boolean
 "alterConstants"(arg0: $ShaderConstants$Type): void
}

export namespace $GameStateProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameStateProvider$Type = ($GameStateProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameStateProvider_ = $GameStateProvider$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/materials/model/$ModelData" {
import {$BasicData, $BasicData$Type} from "packages/com/jozufozu/flywheel/core/materials/$BasicData"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Transform, $Transform$Type} from "packages/com/jozufozu/flywheel/util/transform/$Transform"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Axis, $Axis$Type} from "packages/com/mojang/math/$Axis"

export class $ModelData extends $BasicData implements $Transform<($ModelData)> {
readonly "model": $Matrix4f
readonly "normal": $Matrix3f
 "blockLight": byte
 "skyLight": byte
 "r": byte
 "g": byte
 "b": byte
 "a": byte

constructor()

public "setEmptyTransform"(): $ModelData
public "loadIdentity"(): $ModelData
public "setTransform"(arg0: $PoseStack$Type): $ModelData
public "transform"(arg0: $PoseStack$Type): $ModelData
public "transform"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type): $ModelData
public "rotateCentered"(arg0: $Direction$Type, arg1: float): $ModelData
public "rotateCentered"(arg0: $Quaternionf$Type): $ModelData
public "centre"(): $ModelData
public "translate"(arg0: $Vec3i$Type): $ModelData
public "translate"(arg0: $Vec3$Type): $ModelData
public "translate"(arg0: $Vector3f$Type): $ModelData
public "translateZ"(arg0: double): $ModelData
public "translateAll"(arg0: double): $ModelData
public "unCentre"(): $ModelData
public "translateX"(arg0: double): $ModelData
public "translateY"(arg0: double): $ModelData
public "nudge"(arg0: integer): $ModelData
public "translateBack"(arg0: $Vec3$Type): $ModelData
public "translateBack"(arg0: $Vec3i$Type): $ModelData
public "translateBack"(arg0: double, arg1: double, arg2: double): $ModelData
public "multiply"(arg0: $Axis$Type, arg1: double): $ModelData
public "multiply"(arg0: $Vector3f$Type, arg1: double): $ModelData
public "rotate"(arg0: double, arg1: $Direction$Axis$Type): $ModelData
public "rotate"(arg0: $Direction$Type, arg1: float): $ModelData
public "rotateXRadians"(arg0: double): $ModelData
public "rotateToFace"(arg0: $Direction$Type): $ModelData
public "rotateYRadians"(arg0: double): $ModelData
public "rotateZRadians"(arg0: double): $ModelData
public "multiplyRadians"(arg0: $Axis$Type, arg1: double): $ModelData
public "multiplyRadians"(arg0: $Vector3f$Type, arg1: double): $ModelData
public "rotateZ"(arg0: double): $ModelData
public "rotateX"(arg0: double): $ModelData
public "rotateY"(arg0: double): $ModelData
public "scale"(arg0: float): $ModelData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelData$Type = ($ModelData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelData_ = $ModelData$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/model/$ModelAllocator" {
import {$Model, $Model$Type} from "packages/com/jozufozu/flywheel/core/model/$Model"
import {$ModelAllocator$Callback, $ModelAllocator$Callback$Type} from "packages/com/jozufozu/flywheel/backend/model/$ModelAllocator$Callback"
import {$BufferedModel, $BufferedModel$Type} from "packages/com/jozufozu/flywheel/backend/model/$BufferedModel"

export interface $ModelAllocator {

 "alloc"(arg0: $Model$Type, arg1: $ModelAllocator$Callback$Type): $BufferedModel

(arg0: $Model$Type, arg1: $ModelAllocator$Callback$Type): $BufferedModel
}

export namespace $ModelAllocator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelAllocator$Type = ($ModelAllocator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelAllocator_ = $ModelAllocator$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$FragmentCompiler" {
import {$FragmentTemplateData, $FragmentTemplateData$Type} from "packages/com/jozufozu/flywheel/core/compile/$FragmentTemplateData"
import {$Template, $Template$Type} from "packages/com/jozufozu/flywheel/core/compile/$Template"
import {$FragmentCompiler$Context, $FragmentCompiler$Context$Type} from "packages/com/jozufozu/flywheel/core/compile/$FragmentCompiler$Context"
import {$GlShader, $GlShader$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$GlShader"
import {$Memoizer, $Memoizer$Type} from "packages/com/jozufozu/flywheel/core/compile/$Memoizer"
import {$FileResolution, $FileResolution$Type} from "packages/com/jozufozu/flywheel/core/source/$FileResolution"

export class $FragmentCompiler extends $Memoizer<($FragmentCompiler$Context), ($GlShader)> {

constructor(arg0: $Template$Type<($FragmentTemplateData$Type)>, arg1: $FileResolution$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FragmentCompiler$Type = ($FragmentCompiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FragmentCompiler_ = $FragmentCompiler$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/entity/$EntityInstance" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$LightListener, $LightListener$Type} from "packages/com/jozufozu/flywheel/light/$LightListener"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$TickingLightListener, $TickingLightListener$Type} from "packages/com/jozufozu/flywheel/light/$TickingLightListener"
import {$AbstractInstance, $AbstractInstance$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$AbstractInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GridAlignedBB, $GridAlignedBB$Type} from "packages/com/jozufozu/flywheel/util/box/$GridAlignedBB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export class $EntityInstance<E extends $Entity> extends $AbstractInstance implements $LightListener, $TickingLightListener {
readonly "world": $Level

constructor(arg0: $MaterialManager$Type, arg1: E)

public "getWorldPosition"(): $BlockPos
public "getInstancePosition"(): $Vector3f
public "getInstancePosition"(arg0: float): $Vector3f
public "tickLightListener"(): boolean
public "getVolume"(): $GridAlignedBB
public "onLightUpdate"(arg0: $LightLayer$Type, arg1: $ImmutableBox$Type): void
public "isListenerInvalid"(): boolean
get "worldPosition"(): $BlockPos
get "instancePosition"(): $Vector3f
get "volume"(): $GridAlignedBB
get "listenerInvalid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityInstance$Type<E> = ($EntityInstance<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityInstance_<E> = $EntityInstance$Type<(E)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/$GlObject" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GlObject {

constructor()

public "delete"(): void
public "handle"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlObject$Type = ($GlObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlObject_ = $GlObject$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/model/$ModelAllocator$Callback" {
import {$BufferedModel, $BufferedModel$Type} from "packages/com/jozufozu/flywheel/backend/model/$BufferedModel"

export interface $ModelAllocator$Callback {

 "onAlloc"(arg0: $BufferedModel$Type): void

(arg0: $BufferedModel$Type): void
}

export namespace $ModelAllocator$Callback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelAllocator$Callback$Type = ($ModelAllocator$Callback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelAllocator$Callback_ = $ModelAllocator$Callback$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/$DiffuseLightCalculator" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"

export interface $DiffuseLightCalculator {

 "getDiffuse"(arg0: float, arg1: float, arg2: float, arg3: boolean): float

(arg0: $ClientLevel$Type): $DiffuseLightCalculator
}

export namespace $DiffuseLightCalculator {
const DEFAULT: $DiffuseLightCalculator
const NETHER: $DiffuseLightCalculator
function forLevel(arg0: $ClientLevel$Type): $DiffuseLightCalculator
function forCurrentLevel(): $DiffuseLightCalculator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DiffuseLightCalculator$Type = ($DiffuseLightCalculator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DiffuseLightCalculator_ = $DiffuseLightCalculator$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/vertex/$VertexType" {
import {$VertexList, $VertexList$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexList"
import {$VertexWriter, $VertexWriter$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexWriter"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export interface $VertexType {

 "byteOffset"(arg0: integer): integer
 "getLayout"(): $BufferLayout
 "createWriter"(arg0: $ByteBuffer$Type): $VertexWriter
 "createReader"(arg0: $ByteBuffer$Type, arg1: integer): $VertexList
 "getShaderHeader"(): string
 "getStride"(): integer
}

export namespace $VertexType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexType$Type = ($VertexType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexType_ = $VertexType$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/$Resolver" {
import {$FileResolution, $FileResolution$Type} from "packages/com/jozufozu/flywheel/core/source/$FileResolution"
import {$SourceFinder, $SourceFinder$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFinder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $Resolver {
static readonly "INSTANCE": $Resolver

constructor()

public "run"(arg0: $SourceFinder$Type): void
public "get"(arg0: $ResourceLocation$Type): $FileResolution
public "invalidate"(): void
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
declare module "packages/com/jozufozu/flywheel/core/source/$SourceFile" {
import {$SourceLines, $SourceLines$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceLines"
import {$FileIndex, $FileIndex$Type} from "packages/com/jozufozu/flywheel/core/source/$FileIndex"
import {$ShaderFunction, $ShaderFunction$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$ShaderFunction"
import {$Import, $Import$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$Import"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ShaderSources, $ShaderSources$Type} from "packages/com/jozufozu/flywheel/core/source/$ShaderSources"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$ShaderStruct, $ShaderStruct$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$ShaderStruct"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"

export class $SourceFile {
static readonly "functionDeclaration": $Pattern
readonly "name": $ResourceLocation
readonly "parent": $ShaderSources
readonly "source": string
readonly "elided": string
readonly "lines": $SourceLines
readonly "functions": $ImmutableMap<(string), ($ShaderFunction)>
readonly "structs": $ImmutableMap<(string), ($ShaderStruct)>
readonly "imports": $ImmutableList<($Import)>

constructor(arg0: $ShaderSources$Type, arg1: $ResourceLocation$Type, arg2: string)

public "toString"(): string
public "findFunction"(arg0: charseq): $Optional<($ShaderFunction)>
public "generateFinalSource"(arg0: $FileIndex$Type, arg1: $StringBuilder$Type): void
public "getLineSpanNoWhitespace"(arg0: integer): $Span
public "findStruct"(arg0: charseq): $Optional<($ShaderStruct)>
public "printSource"(): string
public "getLineSpan"(arg0: integer): $Span
public "importStatement"(): charseq
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SourceFile$Type = ($SourceFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SourceFile_ = $SourceFile$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$VertexCompiler$Context" {
import {$VertexType, $VertexType$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexType"
import {$StateSnapshot, $StateSnapshot$Type} from "packages/com/jozufozu/flywheel/core/shader/$StateSnapshot"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"

export class $VertexCompiler$Context {

constructor(arg0: $SourceFile$Type, arg1: $StateSnapshot$Type, arg2: $VertexType$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexCompiler$Context$Type = ($VertexCompiler$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexCompiler$Context_ = $VertexCompiler$Context$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$RenderDispatcher" {
import {$RenderLayerEvent, $RenderLayerEvent$Type} from "packages/com/jozufozu/flywheel/event/$RenderLayerEvent"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$TaskEngine, $TaskEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$TaskEngine"

export interface $RenderDispatcher {

 "delete"(): void
 "render"(arg0: $TaskEngine$Type, arg1: $RenderLayerEvent$Type): void
 "beginFrame"(arg0: $Camera$Type): void
}

export namespace $RenderDispatcher {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderDispatcher$Type = ($RenderDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderDispatcher_ = $RenderDispatcher$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/model/$BufferedModel" {
import {$VertexType, $VertexType$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexType"
import {$GlVertexArray, $GlVertexArray$Type} from "packages/com/jozufozu/flywheel/backend/gl/$GlVertexArray"
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export interface $BufferedModel {

 "delete"(): void
 "getType"(): $VertexType
 "valid"(): boolean
 "getAttributeCount"(): integer
 "getLayout"(): $BufferLayout
 "setupState"(arg0: $GlVertexArray$Type): void
 "drawInstances"(arg0: integer): void
 "drawCall"(): void
 "isDeleted"(): boolean
 "getVertexCount"(): integer
}

export namespace $BufferedModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferedModel$Type = ($BufferedModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferedModel_ = $BufferedModel$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/transform/$TStack" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TStack<Self> {

 "pushPose"(): Self
 "popPose"(): Self
}

export namespace $TStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TStack$Type<Self> = ($TStack<(Self)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TStack_<Self> = $TStack$Type<(Self)>;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$Variable" {
import {$AbstractShaderElement, $AbstractShaderElement$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$AbstractShaderElement"
import {$Variable$Qualifier, $Variable$Qualifier$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$Variable$Qualifier"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $Variable extends $AbstractShaderElement {
readonly "qualifierSpan": $Span
readonly "type": $Span
readonly "name": $Span
readonly "qualifier": $Variable$Qualifier
readonly "self": $Span

constructor(arg0: $Span$Type, arg1: $Span$Type, arg2: $Span$Type, arg3: $Span$Type)

public "toString"(): string
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
declare module "packages/com/jozufozu/flywheel/core/shader/$StateSnapshot" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ShaderConstants, $ShaderConstants$Type} from "packages/com/jozufozu/flywheel/core/shader/$ShaderConstants"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"

export class $StateSnapshot extends $Record {

constructor(ctx: $BitSet$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "ctx"(): $BitSet
public "getShaderConstants"(): $ShaderConstants
get "shaderConstants"(): $ShaderConstants
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StateSnapshot$Type = ($StateSnapshot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StateSnapshot_ = $StateSnapshot$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/buffer/$VecBuffer" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $VecBuffer {

constructor()
constructor(arg0: $ByteBuffer$Type)

public "put"(arg0: byte): $VecBuffer
public "put"(arg0: $FloatBuffer$Type): $VecBuffer
public "put"(arg0: $ByteBuffer$Type): $VecBuffer
public "putShort"(arg0: short): $VecBuffer
public "putInt"(arg0: integer): $VecBuffer
public "putFloat"(arg0: float): $VecBuffer
public "position"(arg0: integer): $VecBuffer
public "position"(): integer
public "unwrap"(): $ByteBuffer
public "rewind"(): $VecBuffer
public static "allocate"(arg0: integer): $VecBuffer
public "putByteArray"(arg0: (byte)[]): $VecBuffer
public "putFloatArray"(arg0: (float)[]): $VecBuffer
public "putVec2"(arg0: float, arg1: float): $VecBuffer
public "putVec2"(arg0: byte, arg1: byte): $VecBuffer
public "putVec3"(arg0: float, arg1: float, arg2: float): $VecBuffer
public "putVec3"(arg0: byte, arg1: byte, arg2: byte): $VecBuffer
public "putVec4"(arg0: float, arg1: float, arg2: float, arg3: float): $VecBuffer
public "putColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $VecBuffer
public "putColor"(arg0: byte, arg1: byte, arg2: byte, arg3: byte): $VecBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VecBuffer$Type = ($VecBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VecBuffer_ = $VecBuffer$Type;
}}
declare module "packages/com/jozufozu/flywheel/mixin/$PausedPartialTickAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PausedPartialTickAccessor {

 "flywheel$getPausePartialTick"(): float

(): float
}

export namespace $PausedPartialTickAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PausedPartialTickAccessor$Type = ($PausedPartialTickAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PausedPartialTickAccessor_ = $PausedPartialTickAccessor$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/blockentity/$BlockEntityTypeExtension" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityInstancingController, $BlockEntityInstancingController$Type} from "packages/com/jozufozu/flywheel/backend/instancing/blockentity/$BlockEntityInstancingController"

export interface $BlockEntityTypeExtension<T extends $BlockEntity> {

 "flywheel$setInstancingController"(arg0: $BlockEntityInstancingController$Type<(any)>): void
 "flywheel$getInstancingController"(): $BlockEntityInstancingController<(any)>
}

export namespace $BlockEntityTypeExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityTypeExtension$Type<T> = ($BlockEntityTypeExtension<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityTypeExtension_<T> = $BlockEntityTypeExtension$Type<(T)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$InstanceManager" {
import {$InstancingEngine$OriginShiftListener, $InstancingEngine$OriginShiftListener$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$OriginShiftListener"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$TaskEngine, $TaskEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$TaskEngine"

export class $InstanceManager<T> implements $InstancingEngine$OriginShiftListener {
readonly "materialManager": $MaterialManager

constructor(arg0: $MaterialManager$Type)

public "add"(arg0: T): void
public "remove"(arg0: T): void
public "update"(arg0: T): void
public "tick"(arg0: $TaskEngine$Type, arg1: double, arg2: double, arg3: double): void
public "invalidate"(): void
public "detachLightListeners"(): void
public "queueUpdate"(arg0: T): void
public "onOriginShift"(): void
public "getObjectCount"(): integer
public "queueAddAll"(arg0: $Collection$Type<(any)>): void
public "queueAdd"(arg0: T): void
public "beginFrame"(arg0: $TaskEngine$Type, arg1: $Camera$Type): void
get "objectCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstanceManager$Type<T> = ($InstanceManager<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstanceManager_<T> = $InstanceManager$Type<(T)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$DrawBuffer" {
import {$DirectVertexConsumer, $DirectVertexConsumer$Type} from "packages/com/jozufozu/flywheel/backend/model/$DirectVertexConsumer"
import {$BufferBuilderExtension, $BufferBuilderExtension$Type} from "packages/com/jozufozu/flywheel/backend/model/$BufferBuilderExtension"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $DrawBuffer {

constructor(arg0: $RenderType$Type)

public "begin"(arg0: integer): $DirectVertexConsumer
public "reset"(): void
public "inject"(arg0: $BufferBuilderExtension$Type): void
public "hasVertices"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawBuffer$Type = ($DrawBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawBuffer_ = $DrawBuffer$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$GPUInstancer" {
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$Model, $Model$Type} from "packages/com/jozufozu/flywheel/core/model/$Model"
import {$AbstractInstancer, $AbstractInstancer$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$AbstractInstancer"
import {$Instanced, $Instanced$Type} from "packages/com/jozufozu/flywheel/api/struct/$Instanced"
import {$ModelAllocator, $ModelAllocator$Type} from "packages/com/jozufozu/flywheel/backend/model/$ModelAllocator"

export class $GPUInstancer<D extends $InstanceData> extends $AbstractInstancer<(D)> {

constructor(arg0: $Instanced$Type<(D)>, arg1: $Model$Type)

public "isEmpty"(): boolean
public "init"(arg0: $ModelAllocator$Type): void
public "delete"(): void
public "render"(): void
public "notifyDirty"(): void
public "isInitialized"(): boolean
get "empty"(): boolean
get "initialized"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GPUInstancer$Type<D> = ($GPUInstancer<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GPUInstancer_<D> = $GPUInstancer$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/blockentity/$BlockEntityInstance" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$AbstractInstance, $AbstractInstance$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$AbstractInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockEntityInstance<T extends $BlockEntity> extends $AbstractInstance {
readonly "world": $Level

constructor(arg0: $MaterialManager$Type, arg1: T)

public "getWorldPosition"(): $BlockPos
public "getInstancePosition"(): $BlockPos
public "shouldReset"(): boolean
public "getVolume"(): $ImmutableBox
get "worldPosition"(): $BlockPos
get "instancePosition"(): $BlockPos
get "volume"(): $ImmutableBox
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityInstance$Type<T> = ($BlockEntityInstance<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityInstance_<T> = $BlockEntityInstance$Type<(T)>;
}}
declare module "packages/com/jozufozu/flywheel/api/vertex/$VertexList" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $VertexList {

 "isEmpty"(): boolean
 "delete"(): void
 "getG"(arg0: integer): byte
 "getY"(arg0: integer): float
 "getLight"(arg0: integer): integer
 "getZ"(arg0: integer): float
 "getX"(arg0: integer): float
 "getA"(arg0: integer): byte
 "getB"(arg0: integer): byte
 "getR"(arg0: integer): byte
 "getNX"(arg0: integer): float
 "getNY"(arg0: integer): float
 "getNZ"(arg0: integer): float
 "getU"(arg0: integer): float
 "getVertexCount"(): integer
 "getV"(arg0: integer): float
}

export namespace $VertexList {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexList$Type = ($VertexList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexList_ = $VertexList$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/$FileResolution" {
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $FileResolution {


public "getFile"(): $SourceFile
public "addSpan"(arg0: $Span$Type): $FileResolution
public "getFileLoc"(): $ResourceLocation
public "addSpec"(arg0: $ResourceLocation$Type): void
get "file"(): $SourceFile
get "fileLoc"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileResolution$Type = ($FileResolution);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileResolution_ = $FileResolution$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/shader/$GlProgram$Factory" {
import {$GlProgram, $GlProgram$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$GlProgram"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $GlProgram$Factory<P extends $GlProgram> {

 "create"(arg0: $ResourceLocation$Type, arg1: integer): P

(arg0: $ResourceLocation$Type, arg1: integer): P
}

export namespace $GlProgram$Factory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlProgram$Factory$Type<P> = ($GlProgram$Factory<(P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlProgram$Factory_<P> = $GlProgram$Factory$Type<(P)>;
}}
declare module "packages/com/jozufozu/flywheel/util/transform/$Rotate" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Axis, $Axis$Type} from "packages/com/mojang/math/$Axis"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Rotate<Self> {

 "multiply"(arg0: $Axis$Type, arg1: double): Self
 "multiply"(arg0: $Vector3f$Type, arg1: double): Self
 "multiply"(arg0: $Quaternionf$Type): Self
 "rotate"(arg0: double, arg1: $Direction$Axis$Type): Self
 "rotate"(arg0: $Direction$Type, arg1: float): Self
 "rotateXRadians"(arg0: double): Self
 "rotateToFace"(arg0: $Direction$Type): Self
 "rotateYRadians"(arg0: double): Self
 "rotateZRadians"(arg0: double): Self
 "multiplyRadians"(arg0: $Axis$Type, arg1: double): Self
 "multiplyRadians"(arg0: $Vector3f$Type, arg1: double): Self
 "rotateZ"(arg0: double): Self
 "rotateX"(arg0: double): Self
 "rotateY"(arg0: double): Self

(arg0: $Axis$Type, arg1: double): Self
}

export namespace $Rotate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rotate$Type<Self> = ($Rotate<(Self)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rotate_<Self> = $Rotate$Type<(Self)>;
}}
declare module "packages/com/jozufozu/flywheel/light/$LightVolume" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$LightListener, $LightListener$Type} from "packages/com/jozufozu/flywheel/light/$LightListener"
import {$GridAlignedBB, $GridAlignedBB$Type} from "packages/com/jozufozu/flywheel/util/box/$GridAlignedBB"
import {$CoordinateConsumer, $CoordinateConsumer$Type} from "packages/com/jozufozu/flywheel/util/box/$CoordinateConsumer"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export class $LightVolume implements $ImmutableBox, $LightListener {

constructor(arg0: $BlockAndTintGetter$Type, arg1: $ImmutableBox$Type)

public "initialize"(): void
public "delete"(): void
public "move"(arg0: $ImmutableBox$Type): void
public "onLightUpdate"(arg0: $LightLayer$Type, arg1: $ImmutableBox$Type): void
public "isListenerInvalid"(): boolean
public "onLightPacket"(arg0: integer, arg1: integer): void
public "getMaxX"(): integer
public "getMinZ"(): integer
public "getMaxZ"(): integer
public "getMinX"(): integer
public "getMaxY"(): integer
public "getMinY"(): integer
public "getVolume"(): $ImmutableBox
public "getPackedLight"(arg0: integer, arg1: integer, arg2: integer): short
public "copyBlock"(arg0: $ImmutableBox$Type): void
public "copyLight"(arg0: $ImmutableBox$Type): void
public "copySky"(arg0: $ImmutableBox$Type): void
public "contains"(arg0: $ImmutableBox$Type): boolean
public "contains"(arg0: integer, arg1: integer, arg2: integer): boolean
public "empty"(): boolean
public "copy"(): $GridAlignedBB
public "union"(arg0: $ImmutableBox$Type): $ImmutableBox
public "intersects"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public "intersects"(arg0: $ImmutableBox$Type): boolean
public "isContainedBy"(arg0: $GridAlignedBB$Type): boolean
public "toAABB"(): $AABB
public "sameAs"(arg0: $ImmutableBox$Type, arg1: integer): boolean
public "sameAs"(arg0: $AABB$Type): boolean
public "sameAs"(arg0: $ImmutableBox$Type): boolean
public "hasPowerOf2Sides"(): boolean
public "forEachContained"(arg0: $CoordinateConsumer$Type): void
public "volume"(): integer
public "intersect"(arg0: $ImmutableBox$Type): $GridAlignedBB
public "sizeY"(): integer
public "sizeX"(): integer
public "sizeZ"(): integer
get "listenerInvalid"(): boolean
get "maxX"(): integer
get "minZ"(): integer
get "maxZ"(): integer
get "minX"(): integer
get "maxY"(): integer
get "minY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightVolume$Type = ($LightVolume);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightVolume_ = $LightVolume$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$StructField" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$AbstractShaderElement, $AbstractShaderElement$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$AbstractShaderElement"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $StructField extends $AbstractShaderElement {
static readonly "fieldPattern": $Pattern
 "type": $Span
 "name": $Span
readonly "self": $Span

constructor(arg0: $Span$Type, arg1: $Span$Type, arg2: $Span$Type)

public "getName"(): $Span
public "toString"(): string
public "getType"(): $Span
get "name"(): $Span
get "type"(): $Span
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructField$Type = ($StructField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructField_ = $StructField$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$FragmentCompiler$Context" {
import {$StateSnapshot, $StateSnapshot$Type} from "packages/com/jozufozu/flywheel/core/shader/$StateSnapshot"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$ShaderConstants, $ShaderConstants$Type} from "packages/com/jozufozu/flywheel/core/shader/$ShaderConstants"

export class $FragmentCompiler$Context {

constructor(arg0: $SourceFile$Type, arg1: $StateSnapshot$Type, arg2: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getShaderConstants"(): $ShaderConstants
get "shaderConstants"(): $ShaderConstants
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FragmentCompiler$Context$Type = ($FragmentCompiler$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FragmentCompiler$Context_ = $FragmentCompiler$Context$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/struct/$Batched" {
import {$StructType, $StructType$Type} from "packages/com/jozufozu/flywheel/api/struct/$StructType"
import {$ModelTransformer$Params, $ModelTransformer$Params$Type} from "packages/com/jozufozu/flywheel/core/model/$ModelTransformer$Params"
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export interface $Batched<S> extends $StructType<(S)> {

 "transform"(arg0: S, arg1: $ModelTransformer$Params$Type): void
 "create"(): S
 "getLayout"(): $BufferLayout
}

export namespace $Batched {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Batched$Type<S> = ($Batched<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Batched_<S> = $Batched$Type<(S)>;
}}
declare module "packages/com/jozufozu/flywheel/event/$ReloadRenderersEvent" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"

export class $ReloadRenderersEvent extends $Event {

constructor()
constructor(arg0: $ClientLevel$Type)

public "isCancelable"(): boolean
public "getWorld"(): $ClientLevel
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "world"(): $ClientLevel
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadRenderersEvent$Type = ($ReloadRenderersEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadRenderersEvent_ = $ReloadRenderersEvent$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/shader/$WorldProgram" {
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$GlProgram, $GlProgram$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$GlProgram"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $WorldProgram extends $GlProgram {
readonly "name": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: integer)

public "bind"(): void
public "uploadViewProjection"(arg0: $Matrix4f$Type): void
public "uploadTime"(arg0: float): void
public "uploadWindowSize"(): void
public "uploadCameraPos"(arg0: double, arg1: double, arg2: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldProgram$Type = ($WorldProgram);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldProgram_ = $WorldProgram$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/shader/$ShaderType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ShaderType extends $Enum<($ShaderType)> {
static readonly "VERTEX": $ShaderType
static readonly "FRAGMENT": $ShaderType
readonly "name": string
readonly "define": string
readonly "glEnum": integer


public static "values"(): ($ShaderType)[]
public static "valueOf"(arg0: string): $ShaderType
public "getDefineStatement"(): string
get "defineStatement"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderType$Type = (("fragment") | ("vertex")) | ($ShaderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderType_ = $ShaderType$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$VertexData" {
import {$VertexType, $VertexType$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexType"
import {$FileIndex, $FileIndex$Type} from "packages/com/jozufozu/flywheel/core/source/$FileIndex"

export interface $VertexData {

 "generateFooter"(arg0: $FileIndex$Type, arg1: $VertexType$Type): string

(arg0: $FileIndex$Type, arg1: $VertexType$Type): string
}

export namespace $VertexData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexData$Type = ($VertexData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexData_ = $VertexData$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/struct/$StructType" {
import {$BufferLayout, $BufferLayout$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout"

export interface $StructType<S> {

 "create"(): S
 "getLayout"(): $BufferLayout
}

export namespace $StructType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructType$Type<S> = ($StructType<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructType_<S> = $StructType$Type<(S)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/entity/$EntityTypeExtension" {
import {$EntityInstancingController, $EntityInstancingController$Type} from "packages/com/jozufozu/flywheel/backend/instancing/entity/$EntityInstancingController"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityTypeExtension<T extends $Entity> {

 "flywheel$setInstancingController"(arg0: $EntityInstancingController$Type<(any)>): void
 "flywheel$getInstancingController"(): $EntityInstancingController<(any)>
}

export namespace $EntityTypeExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeExtension$Type<T> = ($EntityTypeExtension<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeExtension_<T> = $EntityTypeExtension$Type<(T)>;
}}
declare module "packages/com/jozufozu/flywheel/mixin/$BlockEntityRenderDispatcherAccessor" {
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $BlockEntityRenderDispatcherAccessor {

 "flywheel$getRenderers"(): $Map<($BlockEntityType<(any)>), ($BlockEntityRenderer<(any)>)>

(): $Map<($BlockEntityType<(any)>), ($BlockEntityRenderer<(any)>)>
}

export namespace $BlockEntityRenderDispatcherAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityRenderDispatcherAccessor$Type = ($BlockEntityRenderDispatcherAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityRenderDispatcherAccessor_ = $BlockEntityRenderDispatcherAccessor$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/$Material" {
import {$PartialModel, $PartialModel$Type} from "packages/com/jozufozu/flywheel/core/$PartialModel"
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$Model, $Model$Type} from "packages/com/jozufozu/flywheel/core/model/$Model"
import {$Instancer, $Instancer$Type} from "packages/com/jozufozu/flywheel/api/$Instancer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $Material<D extends $InstanceData> {

 "getModel"(arg0: $BlockState$Type): $Instancer<(D)>
 "getModel"(arg0: $PartialModel$Type, arg1: $BlockState$Type, arg2: $Direction$Type, arg3: $Supplier$Type<($PoseStack$Type)>): $Instancer<(D)>
 "getModel"(arg0: $PartialModel$Type, arg1: $BlockState$Type, arg2: $Direction$Type): $Instancer<(D)>
 "getModel"(arg0: $PartialModel$Type): $Instancer<(D)>
 "getModel"(arg0: $PartialModel$Type, arg1: $BlockState$Type): $Instancer<(D)>
 "model"(arg0: any, arg1: $Supplier$Type<($Model$Type)>): $Instancer<(D)>

(arg0: $BlockState$Type): $Instancer<(D)>
}

export namespace $Material {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Material$Type<D> = ($Material<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Material_<D> = $Material$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/api/$Instancer" {
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"

export interface $Instancer<D extends $InstanceData> {

 "createInstance"(): D
 "createInstances"(arg0: (D)[]): void
 "notifyRemoval"(): void
 "stealInstance"(arg0: D): void
 "notifyDirty"(): void
}

export namespace $Instancer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instancer$Type<D> = ($Instancer<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instancer_<D> = $Instancer$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/$TaskEngine" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $TaskEngine {

 "submit"(arg0: $Runnable$Type): void
 "syncPoint"(): void
}

export namespace $TaskEngine {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TaskEngine$Type = ($TaskEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TaskEngine_ = $TaskEngine$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/struct/$BufferWriter" {
import {$StructWriter, $StructWriter$Type} from "packages/com/jozufozu/flywheel/api/struct/$StructWriter"

export class $BufferWriter<S> implements $StructWriter<(S)> {


public "write"(arg0: S): void
public "seek"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferWriter$Type<S> = ($BufferWriter<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferWriter_<S> = $BufferWriter$Type<(S)>;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$ProgramCompiler" {
import {$GlProgram$Factory, $GlProgram$Factory$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$GlProgram$Factory"
import {$FragmentCompiler, $FragmentCompiler$Type} from "packages/com/jozufozu/flywheel/core/compile/$FragmentCompiler"
import {$ReloadRenderersEvent, $ReloadRenderersEvent$Type} from "packages/com/jozufozu/flywheel/event/$ReloadRenderersEvent"
import {$Template, $Template$Type} from "packages/com/jozufozu/flywheel/core/compile/$Template"
import {$Memoizer, $Memoizer$Type} from "packages/com/jozufozu/flywheel/core/compile/$Memoizer"
import {$FileResolution, $FileResolution$Type} from "packages/com/jozufozu/flywheel/core/source/$FileResolution"
import {$ProgramContext, $ProgramContext$Type} from "packages/com/jozufozu/flywheel/core/compile/$ProgramContext"
import {$VertexCompiler, $VertexCompiler$Type} from "packages/com/jozufozu/flywheel/core/compile/$VertexCompiler"
import {$GlProgram, $GlProgram$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$GlProgram"
import {$VertexData, $VertexData$Type} from "packages/com/jozufozu/flywheel/core/compile/$VertexData"

export class $ProgramCompiler<P extends $GlProgram> extends $Memoizer<($ProgramContext), (P)> {

constructor(arg0: $GlProgram$Factory$Type<(P)>, arg1: $VertexCompiler$Type, arg2: $FragmentCompiler$Type)

public static "create"<T extends $VertexData, P extends $GlProgram>(arg0: $Template$Type<(T)>, arg1: $GlProgram$Factory$Type<(P)>, arg2: $FileResolution$Type): $ProgramCompiler<(P)>
public "invalidate"(): void
public "getProgram"(arg0: $ProgramContext$Type): P
public static "invalidateAll"(arg0: $ReloadRenderersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgramCompiler$Type<P> = ($ProgramCompiler<(P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgramCompiler_<P> = $ProgramCompiler$Type<(P)>;
}}
declare module "packages/com/jozufozu/flywheel/core/$PartialModel" {
import {$ModelEvent$BakingCompleted, $ModelEvent$BakingCompleted$Type} from "packages/net/minecraftforge/client/event/$ModelEvent$BakingCompleted"
import {$ModelEvent$RegisterAdditional, $ModelEvent$RegisterAdditional$Type} from "packages/net/minecraftforge/client/event/$ModelEvent$RegisterAdditional"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PartialModel {

constructor(arg0: $ResourceLocation$Type)

public "get"(): $BakedModel
public "getLocation"(): $ResourceLocation
public static "onModelBake"(arg0: $ModelEvent$BakingCompleted$Type): void
public static "onModelRegistry"(arg0: $ModelEvent$RegisterAdditional$Type): void
get "location"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PartialModel$Type = ($PartialModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PartialModel_ = $PartialModel$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/shader/$ProgramSpec" {
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$FileResolution, $FileResolution$Type} from "packages/com/jozufozu/flywheel/core/source/$FileResolution"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ProgramSpec {
static readonly "CODEC": $Codec<($ProgramSpec)>
 "name": $ResourceLocation
readonly "vertex": $FileResolution
readonly "fragment": $FileResolution

constructor(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type)

public "toString"(): string
public "setName"(arg0: $ResourceLocation$Type): void
public "getFragmentFile"(): $SourceFile
public "getVertexFile"(): $SourceFile
public "getFragmentLoc"(): $ResourceLocation
public "getSourceLoc"(): $ResourceLocation
set "name"(value: $ResourceLocation$Type)
get "fragmentFile"(): $SourceFile
get "vertexFile"(): $SourceFile
get "fragmentLoc"(): $ResourceLocation
get "sourceLoc"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgramSpec$Type = ($ProgramSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgramSpec_ = $ProgramSpec$Type;
}}
declare module "packages/com/jozufozu/flywheel/light/$GPULightVolume" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$LightVolume, $LightVolume$Type} from "packages/com/jozufozu/flywheel/light/$LightVolume"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $GPULightVolume extends $LightVolume {

constructor(arg0: $BlockAndTintGetter$Type, arg1: $ImmutableBox$Type)

public "delete"(): void
public "bind"(): void
public "move"(arg0: $ImmutableBox$Type): void
public "getVolume"(): $ImmutableBox
public "unbind"(): void
get "volume"(): $ImmutableBox
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GPULightVolume$Type = ($GPULightVolume);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GPULightVolume_ = $GPULightVolume$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/span/$Span" {
import {$Matcher, $Matcher$Type} from "packages/java/util/regex/$Matcher"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$CharPos, $CharPos$Type} from "packages/com/jozufozu/flywheel/core/source/span/$CharPos"

export class $Span implements charseq {

constructor(arg0: $SourceFile$Type, arg1: integer, arg2: integer)
constructor(arg0: $SourceFile$Type, arg1: $CharPos$Type, arg2: $CharPos$Type)

public "get"(): string
public "length"(): integer
public "toString"(): string
public "charAt"(arg0: integer): character
public "isEmpty"(): boolean
public "lines"(): integer
public "subSequence"(arg0: integer, arg1: integer): charseq
public "getEnd"(): $CharPos
public "firstLine"(): integer
public "getSourceFile"(): $SourceFile
public "getStart"(): $CharPos
public static "fromMatcher"(arg0: $Span$Type, arg1: $Matcher$Type, arg2: integer): $Span
public static "fromMatcher"(arg0: $SourceFile$Type, arg1: $Matcher$Type, arg2: integer): $Span
public static "fromMatcher"(arg0: $SourceFile$Type, arg1: $Matcher$Type): $Span
public static "fromMatcher"(arg0: $Span$Type, arg1: $Matcher$Type): $Span
public "getEndPos"(): integer
public "getStartPos"(): integer
public "subSpan"(arg0: integer, arg1: integer): $Span
public "isErr"(): boolean
public static "compare"(arg0: charseq, arg1: charseq): integer
public "codePoints"(): $IntStream
public "chars"(): $IntStream
get "empty"(): boolean
get "end"(): $CharPos
get "sourceFile"(): $SourceFile
get "start"(): $CharPos
get "endPos"(): integer
get "startPos"(): integer
get "err"(): boolean
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
declare module "packages/com/jozufozu/flywheel/backend/instancing/$AbstractInstance" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$LightListener, $LightListener$Type} from "packages/com/jozufozu/flywheel/light/$LightListener"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Instance, $Instance$Type} from "packages/com/jozufozu/flywheel/api/instance/$Instance"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export class $AbstractInstance implements $Instance, $LightListener {
readonly "world": $Level

constructor(arg0: $MaterialManager$Type, arg1: $Level$Type)

public "update"(): void
public "init"(): void
public "onLightUpdate"(arg0: $LightLayer$Type, arg1: $ImmutableBox$Type): void
public "updateLight"(): void
public "isListenerInvalid"(): boolean
public "removeAndMark"(): void
public "shouldReset"(): boolean
public "getWorldPosition"(): $BlockPos
public "onLightPacket"(arg0: integer, arg1: integer): void
public "getVolume"(): $ImmutableBox
get "listenerInvalid"(): boolean
get "worldPosition"(): $BlockPos
get "volume"(): $ImmutableBox
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractInstance$Type = ($AbstractInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractInstance_ = $AbstractInstance$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/shader/$ShaderConstants" {
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ShaderConstants {

constructor()

public "build"(): string
public "writeInto"(arg0: $StringBuilder$Type): void
public "define"(arg0: string, arg1: float): $ShaderConstants
public "define"(arg0: string): $ShaderConstants
public "define"(arg0: string, arg1: string): $ShaderConstants
public "defineAll"(arg0: $List$Type<(string)>): $ShaderConstants
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderConstants$Type = ($ShaderConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderConstants_ = $ShaderConstants$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$FragmentTemplateData" {
import {$ShaderStruct, $ShaderStruct$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$ShaderStruct"
import {$ShaderFunction, $ShaderFunction$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$ShaderFunction"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$FragmentData, $FragmentData$Type} from "packages/com/jozufozu/flywheel/core/compile/$FragmentData"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $FragmentTemplateData implements $FragmentData {
readonly "file": $SourceFile
readonly "interpolantName": $Span
readonly "interpolant": $ShaderStruct
readonly "fragmentMain": $ShaderFunction

constructor(arg0: $SourceFile$Type)

public "generateFooter"(): string
public static "prefixFields"(arg0: $StringBuilder$Type, arg1: $ShaderStruct$Type, arg2: string, arg3: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FragmentTemplateData$Type = ($FragmentTemplateData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FragmentTemplateData_ = $FragmentTemplateData$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/$SourceLines" {
import {$CharPos, $CharPos$Type} from "packages/com/jozufozu/flywheel/core/source/span/$CharPos"

export class $SourceLines {

constructor(arg0: string)

public "getLine"(arg0: integer): string
public "printLinesWithNumbers"(): string
public "getLineStart"(arg0: integer): integer
public "getLineCount"(): integer
public "getCharPos"(arg0: integer): $CharPos
get "lineCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SourceLines$Type = ($SourceLines);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SourceLines_ = $SourceLines$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/model/$ModelTransformer$Params" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ModelTransformer$SpriteShiftFunc, $ModelTransformer$SpriteShiftFunc$Type} from "packages/com/jozufozu/flywheel/core/model/$ModelTransformer$SpriteShiftFunc"
import {$Transform, $Transform$Type} from "packages/com/jozufozu/flywheel/util/transform/$Transform"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Axis, $Axis$Type} from "packages/com/mojang/math/$Axis"

export class $ModelTransformer$Params implements $Transform<($ModelTransformer$Params)> {
readonly "model": $Matrix4f
readonly "normal": $Matrix3f
 "useParamColor": boolean
 "r": integer
 "g": integer
 "b": integer
 "a": integer
 "spriteShiftFunc": $ModelTransformer$SpriteShiftFunc
 "overlay": integer
 "useParamLight": boolean
 "packedLightCoords": integer

constructor()

public "load"(arg0: $ModelTransformer$Params$Type): void
public "scale"(arg0: float, arg1: float, arg2: float): $ModelTransformer$Params
public "color"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $ModelTransformer$Params
public "color"(arg0: integer): $ModelTransformer$Params
public "color"(arg0: byte, arg1: byte, arg2: byte, arg3: byte): $ModelTransformer$Params
public "multiply"(arg0: $Quaternionf$Type): $ModelTransformer$Params
public "loadDefault"(): void
public "overlay"(arg0: integer): $ModelTransformer$Params
public "light"(arg0: integer): $ModelTransformer$Params
public "shiftUV"(arg0: $ModelTransformer$SpriteShiftFunc$Type): $ModelTransformer$Params
public "mulPose"(arg0: $Matrix4f$Type): $ModelTransformer$Params
public "transform"(arg0: $PoseStack$Type): $ModelTransformer$Params
public "transform"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type): $ModelTransformer$Params
public "rotateCentered"(arg0: $Direction$Type, arg1: float): $ModelTransformer$Params
public "rotateCentered"(arg0: $Quaternionf$Type): $ModelTransformer$Params
public "centre"(): $ModelTransformer$Params
public "translate"(arg0: $Vec3i$Type): $ModelTransformer$Params
public "translate"(arg0: $Vec3$Type): $ModelTransformer$Params
public "translate"(arg0: $Vector3f$Type): $ModelTransformer$Params
public "translateZ"(arg0: double): $ModelTransformer$Params
public "translateAll"(arg0: double): $ModelTransformer$Params
public "unCentre"(): $ModelTransformer$Params
public "translateX"(arg0: double): $ModelTransformer$Params
public "translateY"(arg0: double): $ModelTransformer$Params
public "nudge"(arg0: integer): $ModelTransformer$Params
public "translateBack"(arg0: $Vec3$Type): $ModelTransformer$Params
public "translateBack"(arg0: $Vec3i$Type): $ModelTransformer$Params
public "translateBack"(arg0: double, arg1: double, arg2: double): $ModelTransformer$Params
public "multiply"(arg0: $Axis$Type, arg1: double): $ModelTransformer$Params
public "multiply"(arg0: $Vector3f$Type, arg1: double): $ModelTransformer$Params
public "rotate"(arg0: double, arg1: $Direction$Axis$Type): $ModelTransformer$Params
public "rotate"(arg0: $Direction$Type, arg1: float): $ModelTransformer$Params
public "rotateXRadians"(arg0: double): $ModelTransformer$Params
public "rotateToFace"(arg0: $Direction$Type): $ModelTransformer$Params
public "rotateYRadians"(arg0: double): $ModelTransformer$Params
public "rotateZRadians"(arg0: double): $ModelTransformer$Params
public "multiplyRadians"(arg0: $Axis$Type, arg1: double): $ModelTransformer$Params
public "multiplyRadians"(arg0: $Vector3f$Type, arg1: double): $ModelTransformer$Params
public "rotateZ"(arg0: double): $ModelTransformer$Params
public "rotateX"(arg0: double): $ModelTransformer$Params
public "rotateY"(arg0: double): $ModelTransformer$Params
public "scale"(arg0: float): $ModelTransformer$Params
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelTransformer$Params$Type = ($ModelTransformer$Params);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelTransformer$Params_ = $ModelTransformer$Params$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/model/$Model" {
import {$VertexType, $VertexType$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexType"
import {$VertexList, $VertexList$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexList"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ElementBuffer, $ElementBuffer$Type} from "packages/com/jozufozu/flywheel/backend/model/$ElementBuffer"

export interface $Model {

 "name"(): string
 "size"(): integer
 "empty"(): boolean
 "delete"(): void
 "getType"(): $VertexType
 "vertexCount"(): integer
 "createEBO"(): $ElementBuffer
 "writeInto"(arg0: $ByteBuffer$Type): void
 "getReader"(): $VertexList
}

export namespace $Model {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Model$Type = ($Model);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Model_ = $Model$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/$RenderLayer" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $RenderLayer extends $Enum<($RenderLayer)> {
static readonly "SOLID": $RenderLayer
static readonly "CUTOUT": $RenderLayer
static readonly "TRANSPARENT": $RenderLayer


public static "values"(): ($RenderLayer)[]
public static "valueOf"(arg0: string): $RenderLayer
public static "getLayer"(arg0: $RenderType$Type): $RenderLayer
public static "getPrimaryLayer"(arg0: $RenderType$Type): $RenderLayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLayer$Type = (("solid") | ("cutout") | ("transparent")) | ($RenderLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLayer_ = $RenderLayer$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$ProgramContext" {
import {$VertexType, $VertexType$Type} from "packages/com/jozufozu/flywheel/api/vertex/$VertexType"
import {$RenderLayer, $RenderLayer$Type} from "packages/com/jozufozu/flywheel/backend/$RenderLayer"
import {$StateSnapshot, $StateSnapshot$Type} from "packages/com/jozufozu/flywheel/core/shader/$StateSnapshot"
import {$ProgramSpec, $ProgramSpec$Type} from "packages/com/jozufozu/flywheel/core/shader/$ProgramSpec"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ProgramContext {
readonly "spec": $ProgramSpec
readonly "alphaDiscard": float
readonly "vertexType": $VertexType
readonly "ctx": $StateSnapshot

constructor(arg0: $ProgramSpec$Type, arg1: float, arg2: $VertexType$Type, arg3: $StateSnapshot$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: $ResourceLocation$Type, arg1: $VertexType$Type, arg2: $RenderLayer$Type): $ProgramContext
public static "getAlphaDiscard"(arg0: $RenderLayer$Type): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgramContext$Type = ($ProgramContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgramContext_ = $ProgramContext$Type;
}}
declare module "packages/com/jozufozu/flywheel/util/transform/$Transform" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Scale, $Scale$Type} from "packages/com/jozufozu/flywheel/util/transform/$Scale"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Rotate, $Rotate$Type} from "packages/com/jozufozu/flywheel/util/transform/$Rotate"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Translate, $Translate$Type} from "packages/com/jozufozu/flywheel/util/transform/$Translate"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Axis, $Axis$Type} from "packages/com/mojang/math/$Axis"

export interface $Transform<Self extends $Transform<(Self)>> extends $Translate<(Self)>, $Rotate<(Self)>, $Scale<(Self)> {

 "transform"(arg0: $PoseStack$Type): Self
 "transform"(arg0: $Matrix4f$Type, arg1: $Matrix3f$Type): Self
 "mulPose"(arg0: $Matrix4f$Type): Self
 "rotateCentered"(arg0: $Direction$Type, arg1: float): Self
 "rotateCentered"(arg0: $Quaternionf$Type): Self
 "mulNormal"(arg0: $Matrix3f$Type): Self
 "centre"(): Self
 "translate"(arg0: $Vec3i$Type): Self
 "translate"(arg0: $Vec3$Type): Self
 "translate"(arg0: $Vector3f$Type): Self
 "translate"(arg0: double, arg1: double, arg2: double): Self
 "translateZ"(arg0: double): Self
 "translateAll"(arg0: double): Self
 "unCentre"(): Self
 "translateX"(arg0: double): Self
 "translateY"(arg0: double): Self
 "nudge"(arg0: integer): Self
 "translateBack"(arg0: $Vec3$Type): Self
 "translateBack"(arg0: $Vec3i$Type): Self
 "translateBack"(arg0: double, arg1: double, arg2: double): Self
 "multiply"(arg0: $Axis$Type, arg1: double): Self
 "multiply"(arg0: $Vector3f$Type, arg1: double): Self
 "multiply"(arg0: $Quaternionf$Type): Self
 "rotate"(arg0: double, arg1: $Direction$Axis$Type): Self
 "rotate"(arg0: $Direction$Type, arg1: float): Self
 "rotateXRadians"(arg0: double): Self
 "rotateToFace"(arg0: $Direction$Type): Self
 "rotateYRadians"(arg0: double): Self
 "rotateZRadians"(arg0: double): Self
 "multiplyRadians"(arg0: $Axis$Type, arg1: double): Self
 "multiplyRadians"(arg0: $Vector3f$Type, arg1: double): Self
 "rotateZ"(arg0: double): Self
 "rotateX"(arg0: double): Self
 "rotateY"(arg0: double): Self
 "scale"(arg0: float, arg1: float, arg2: float): Self
 "scale"(arg0: float): Self
}

export namespace $Transform {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Transform$Type<Self> = ($Transform<(Self)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Transform_<Self> = $Transform$Type<(Self)>;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$Memoizer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Memoizer<K, V> {

constructor()

public "get"(arg0: K): V
public "invalidate"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Memoizer$Type<K, V> = ($Memoizer<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Memoizer_<K, V> = $Memoizer$Type<(K), (V)>;
}}
declare module "packages/com/jozufozu/flywheel/util/transform/$Scale" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Scale<Self> {

 "scale"(arg0: float, arg1: float, arg2: float): Self
 "scale"(arg0: float): Self

(arg0: float, arg1: float, arg2: float): Self
}

export namespace $Scale {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Scale$Type<Self> = ($Scale<(Self)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Scale_<Self> = $Scale$Type<(Self)>;
}}
declare module "packages/com/jozufozu/flywheel/core/layout/$BufferLayout" {
import {$LayoutItem, $LayoutItem$Type} from "packages/com/jozufozu/flywheel/core/layout/$LayoutItem"
import {$BufferLayout$Builder, $BufferLayout$Builder$Type} from "packages/com/jozufozu/flywheel/core/layout/$BufferLayout$Builder"
import {$List, $List$Type} from "packages/java/util/$List"

export class $BufferLayout {

constructor(arg0: $List$Type<($LayoutItem$Type)>)

public static "builder"(): $BufferLayout$Builder
public "getAttributeCount"(): integer
public "getLayoutItems"(): $List<($LayoutItem)>
public "getStride"(): integer
get "attributeCount"(): integer
get "layoutItems"(): $List<($LayoutItem)>
get "stride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferLayout$Type = ($BufferLayout);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferLayout_ = $BufferLayout$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine$OriginShiftListener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $InstancingEngine$OriginShiftListener {

 "onOriginShift"(): void

(): void
}

export namespace $InstancingEngine$OriginShiftListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancingEngine$OriginShiftListener$Type = ($InstancingEngine$OriginShiftListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancingEngine$OriginShiftListener_ = $InstancingEngine$OriginShiftListener$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/$FileIndex" {
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export interface $FileIndex {

 "getFile"(arg0: integer): $SourceFile
 "getLineSpan"(arg0: integer, arg1: integer): $Span
 "getFileID"(arg0: $SourceFile$Type): integer
}

export namespace $FileIndex {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileIndex$Type = ($FileIndex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileIndex_ = $FileIndex$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/instance/$Instance" {
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $Instance {

 "getWorldPosition"(): $BlockPos

(): $BlockPos
}

export namespace $Instance {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instance$Type = ($Instance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instance_ = $Instance$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/span/$CharPos" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $CharPos extends $Record {

constructor(pos: integer, line: integer, col: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "line"(): integer
public "pos"(): integer
public "col"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharPos$Type = ($CharPos);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharPos_ = $CharPos$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/shader/$GlShader" {
import {$GlObject, $GlObject$Type} from "packages/com/jozufozu/flywheel/backend/gl/$GlObject"
import {$ShaderType, $ShaderType$Type} from "packages/com/jozufozu/flywheel/backend/gl/shader/$ShaderType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GlShader extends $GlObject {
readonly "name": $ResourceLocation
readonly "type": $ShaderType

constructor(arg0: $ResourceLocation$Type, arg1: $ShaderType$Type, arg2: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlShader$Type = ($GlShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlShader_ = $GlShader$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$AbstractShaderElement" {
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $AbstractShaderElement {
readonly "self": $Span

constructor(arg0: $Span$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractShaderElement$Type = ($AbstractShaderElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractShaderElement_ = $AbstractShaderElement$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/instance/$TickableInstance" {
import {$Instance, $Instance$Type} from "packages/com/jozufozu/flywheel/api/instance/$Instance"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $TickableInstance extends $Instance {

 "tick"(): void
 "decreaseTickRateWithDistance"(): boolean
 "getWorldPosition"(): $BlockPos
}

export namespace $TickableInstance {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickableInstance$Type = ($TickableInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickableInstance_ = $TickableInstance$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/$StitchedSprite" {
import {$TextureStitchEvent$Post, $TextureStitchEvent$Post$Type} from "packages/net/minecraftforge/client/event/$TextureStitchEvent$Post"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $StitchedSprite {

constructor(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type)
constructor(arg0: $ResourceLocation$Type)

public "get"(): $TextureAtlasSprite
public "getLocation"(): $ResourceLocation
public static "onTextureStitchPost"(arg0: $TextureStitchEvent$Post$Type): void
public "getAtlasLocation"(): $ResourceLocation
get "location"(): $ResourceLocation
get "atlasLocation"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StitchedSprite$Type = ($StitchedSprite);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StitchedSprite_ = $StitchedSprite$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/materials/$FlatLit" {
import {$InstanceData, $InstanceData$Type} from "packages/com/jozufozu/flywheel/api/$InstanceData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export interface $FlatLit<D extends ($InstanceData) & ($FlatLit<(D)>)> {

 "setBlockLight"(arg0: integer): D
 "updateLight"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type): D
 "setSkyLight"(arg0: integer): D
 "getPackedLight"(): integer
}

export namespace $FlatLit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlatLit$Type<D> = ($FlatLit<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlatLit_<D> = $FlatLit$Type<(D)>;
}}
declare module "packages/com/jozufozu/flywheel/event/$GatherContextEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$IModBusEvent, $IModBusEvent$Type} from "packages/net/minecraftforge/fml/event/$IModBusEvent"

export class $GatherContextEvent extends $Event implements $IModBusEvent {

constructor()
constructor(arg0: boolean)

public "isCancelable"(): boolean
public "isFirstLoad"(): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "firstLoad"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherContextEvent$Type = ($GatherContextEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherContextEvent_ = $GatherContextEvent$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/parse/$ShaderFunction" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Variable, $Variable$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$Variable"
import {$AbstractShaderElement, $AbstractShaderElement$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$AbstractShaderElement"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$Span, $Span$Type} from "packages/com/jozufozu/flywheel/core/source/span/$Span"

export class $ShaderFunction extends $AbstractShaderElement {
static readonly "argument": $Pattern
static readonly "assignment": $Pattern
readonly "self": $Span

constructor(arg0: $Span$Type, arg1: $Span$Type, arg2: $Span$Type, arg3: $Span$Type, arg4: $Span$Type)

public "getName"(): $Span
public "toString"(): string
public "getType"(): $Span
public "getParameters"(): $ImmutableList<($Variable)>
public "call"(...arg0: (string)[]): string
public "getBody"(): $Span
public "getArgs"(): $Span
public "returnTypeName"(): string
get "name"(): $Span
get "type"(): $Span
get "parameters"(): $ImmutableList<($Variable)>
get "body"(): $Span
get "args"(): $Span
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderFunction$Type = ($ShaderFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderFunction_ = $ShaderFunction$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/source/$Index" {
import {$ShaderStruct, $ShaderStruct$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$ShaderStruct"
import {$ShaderFunction, $ShaderFunction$Type} from "packages/com/jozufozu/flywheel/core/source/parse/$ShaderFunction"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Index {

constructor(arg0: $Map$Type<($ResourceLocation$Type), ($SourceFile$Type)>)

public "getStructDefinitionsMatching"(arg0: charseq): $Collection<($ShaderStruct)>
public "getFunctionDefinitionsMatching"(arg0: charseq): $Collection<($ShaderFunction)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Index$Type = ($Index);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Index_ = $Index$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancedMaterialGroup" {
import {$RenderLayer, $RenderLayer$Type} from "packages/com/jozufozu/flywheel/backend/$RenderLayer"
import {$WorldProgram, $WorldProgram$Type} from "packages/com/jozufozu/flywheel/core/shader/$WorldProgram"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$MaterialGroup, $MaterialGroup$Type} from "packages/com/jozufozu/flywheel/api/$MaterialGroup"
import {$InstancingEngine, $InstancingEngine$Type} from "packages/com/jozufozu/flywheel/backend/instancing/instancing/$InstancingEngine"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $InstancedMaterialGroup<P extends $WorldProgram> implements $MaterialGroup {

constructor(arg0: $InstancingEngine$Type<(P)>, arg1: $RenderType$Type)

public "clear"(): void
public "delete"(): void
public "render"(arg0: $Matrix4f$Type, arg1: double, arg2: double, arg3: double, arg4: $RenderLayer$Type): void
public "getInstanceCount"(): integer
public "getVertexCount"(): integer
get "instanceCount"(): integer
get "vertexCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancedMaterialGroup$Type<P> = ($InstancedMaterialGroup<(P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancedMaterialGroup_<P> = $InstancedMaterialGroup$Type<(P)>;
}}
declare module "packages/com/jozufozu/flywheel/api/$FlywheelWorld" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FlywheelWorld {

 "supportsFlywheel"(): boolean
}

export namespace $FlywheelWorld {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlywheelWorld$Type = ($FlywheelWorld);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlywheelWorld_ = $FlywheelWorld$Type;
}}
declare module "packages/com/jozufozu/flywheel/mixin/$LevelRendererAccessor" {
import {$Long2ObjectMap, $Long2ObjectMap$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2ObjectMap"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$BlockDestructionProgress, $BlockDestructionProgress$Type} from "packages/net/minecraft/server/level/$BlockDestructionProgress"

export interface $LevelRendererAccessor {

 "flywheel$getDestructionProgress"(): $Long2ObjectMap<($SortedSet<($BlockDestructionProgress)>)>

(): $Long2ObjectMap<($SortedSet<($BlockDestructionProgress)>)>
}

export namespace $LevelRendererAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LevelRendererAccessor$Type = ($LevelRendererAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LevelRendererAccessor_ = $LevelRendererAccessor$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/layout/$LayoutItem" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LayoutItem {

 "size"(): integer
 "attributeCount"(): integer
 "vertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer): void
}

export namespace $LayoutItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayoutItem$Type = ($LayoutItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayoutItem_ = $LayoutItem$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/gl/shader/$GlProgram" {
import {$GlObject, $GlObject$Type} from "packages/com/jozufozu/flywheel/backend/gl/$GlObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GlProgram extends $GlObject {
readonly "name": $ResourceLocation


public "toString"(): string
public "bind"(): void
public static "unbind"(): void
public "getUniformLocation"(arg0: string): integer
public "setSamplerBinding"(arg0: string, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlProgram$Type = ($GlProgram);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlProgram_ = $GlProgram$Type;
}}
declare module "packages/com/jozufozu/flywheel/api/struct/$StructWriter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $StructWriter<S> {

 "write"(arg0: S): void
 "seek"(arg0: integer): void
}

export namespace $StructWriter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructWriter$Type<S> = ($StructWriter<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructWriter_<S> = $StructWriter$Type<(S)>;
}}
declare module "packages/com/jozufozu/flywheel/util/box/$GridAlignedBB" {
import {$ImmutableBox, $ImmutableBox$Type} from "packages/com/jozufozu/flywheel/util/box/$ImmutableBox"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$CoordinateConsumer, $CoordinateConsumer$Type} from "packages/com/jozufozu/flywheel/util/box/$CoordinateConsumer"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $GridAlignedBB implements $ImmutableBox {

constructor()
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "contains"(arg0: $ImmutableBox$Type): boolean
public static "from"(arg0: $AABB$Type): $GridAlignedBB
public static "from"(arg0: integer, arg1: integer): $GridAlignedBB
public static "from"(arg0: $BlockPos$Type, arg1: $BlockPos$Type): $GridAlignedBB
public static "from"(arg0: $SectionPos$Type): $GridAlignedBB
public static "from"(arg0: $BlockPos$Type): $GridAlignedBB
public "empty"(): boolean
public "copy"(): $GridAlignedBB
public "grow"(arg0: integer): void
public "grow"(arg0: integer, arg1: integer, arg2: integer): void
public "union"(arg0: $ImmutableBox$Type): $ImmutableBox
public "intersects"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public "setMin"(arg0: $Vec3i$Type): $GridAlignedBB
public "setMin"(arg0: integer, arg1: integer, arg2: integer): $GridAlignedBB
public "getMaxX"(): integer
public "getMinZ"(): integer
public "getMaxZ"(): integer
public "getMinX"(): integer
public "getMaxY"(): integer
public "getMinY"(): integer
public "toAABB"(): $AABB
public "sameAs"(arg0: $ImmutableBox$Type): boolean
public "sameAs"(arg0: $AABB$Type): boolean
public "forEachContained"(arg0: $CoordinateConsumer$Type): void
public "translate"(arg0: $Vec3i$Type): void
public "translate"(arg0: integer, arg1: integer, arg2: integer): void
public "intersect"(arg0: $ImmutableBox$Type): $GridAlignedBB
public "fixMinMax"(): void
public static "ofRadius"(arg0: integer): $GridAlignedBB
public "intersectAssign"(arg0: $ImmutableBox$Type): void
public "unionAssign"(arg0: $AABB$Type): void
public "unionAssign"(arg0: $ImmutableBox$Type): void
public "mirrorAbout"(arg0: $Direction$Axis$Type): void
public "nextPowerOf2"(): void
public "setMinY"(arg0: integer): $GridAlignedBB
public "setMinZ"(arg0: integer): $GridAlignedBB
public "setMaxX"(arg0: integer): $GridAlignedBB
public "setMinX"(arg0: integer): $GridAlignedBB
public "setMaxY"(arg0: integer): $GridAlignedBB
public "setMaxZ"(arg0: integer): $GridAlignedBB
public "assign"(arg0: $BlockPos$Type, arg1: $BlockPos$Type): $GridAlignedBB
public "assign"(arg0: $ImmutableBox$Type): void
public "assign"(arg0: $AABB$Type): void
public static "containingAll"(arg0: $Collection$Type<($BlockPos$Type)>): $ImmutableBox
public "setMax"(arg0: $Vec3i$Type): $GridAlignedBB
public "setMax"(arg0: integer, arg1: integer, arg2: integer): $GridAlignedBB
public "nextPowerOf2Centered"(): void
public "sizeY"(): integer
public "sizeX"(): integer
public "sizeZ"(): integer
public "contains"(arg0: integer, arg1: integer, arg2: integer): boolean
public "intersects"(arg0: $ImmutableBox$Type): boolean
public "isContainedBy"(arg0: $GridAlignedBB$Type): boolean
public "sameAs"(arg0: $ImmutableBox$Type, arg1: integer): boolean
public "hasPowerOf2Sides"(): boolean
public "volume"(): integer
set "min"(value: $Vec3i$Type)
get "maxX"(): integer
get "minZ"(): integer
get "maxZ"(): integer
get "minX"(): integer
get "maxY"(): integer
get "minY"(): integer
set "minY"(value: integer)
set "minZ"(value: integer)
set "maxX"(value: integer)
set "minX"(value: integer)
set "maxY"(value: integer)
set "maxZ"(value: integer)
set "max"(value: $Vec3i$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridAlignedBB$Type = ($GridAlignedBB);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridAlignedBB_ = $GridAlignedBB$Type;
}}
declare module "packages/com/jozufozu/flywheel/backend/model/$DirectVertexConsumer" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $DirectVertexConsumer implements $VertexConsumer {
readonly "format": $VertexFormat
readonly "startPos": integer

constructor(arg0: $ByteBuffer$Type, arg1: $VertexFormat$Type, arg2: integer)

public "split"(arg0: integer): $DirectVertexConsumer
public "vertex"(arg0: double, arg1: double, arg2: double): $VertexConsumer
public "uv"(arg0: float, arg1: float): $VertexConsumer
public "color"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $VertexConsumer
public "normal"(arg0: float, arg1: float, arg2: float): $VertexConsumer
public "overlayCoords"(arg0: integer, arg1: integer): $VertexConsumer
public "uv2"(arg0: integer, arg1: integer): $VertexConsumer
public "defaultColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "unsetDefaultColor"(): void
public "endVertex"(): void
public "hasOverlay"(): boolean
public "memSetZero"(): void
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
export type $DirectVertexConsumer$Type = ($DirectVertexConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectVertexConsumer_ = $DirectVertexConsumer$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/model/$ModelTransformer$SpriteShiftFunc" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"

export interface $ModelTransformer$SpriteShiftFunc {

 "shift"(arg0: $VertexConsumer$Type, arg1: float, arg2: float): void

(arg0: $VertexConsumer$Type, arg1: float, arg2: float): void
}

export namespace $ModelTransformer$SpriteShiftFunc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelTransformer$SpriteShiftFunc$Type = ($ModelTransformer$SpriteShiftFunc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelTransformer$SpriteShiftFunc_ = $ModelTransformer$SpriteShiftFunc$Type;
}}
declare module "packages/com/jozufozu/flywheel/core/compile/$Template" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$SourceFile, $SourceFile$Type} from "packages/com/jozufozu/flywheel/core/source/$SourceFile"
import {$Memoizer, $Memoizer$Type} from "packages/com/jozufozu/flywheel/core/compile/$Memoizer"
import {$GLSLVersion, $GLSLVersion$Type} from "packages/com/jozufozu/flywheel/backend/gl/$GLSLVersion"

export class $Template<T> extends $Memoizer<($SourceFile), (T)> {

constructor(arg0: $GLSLVersion$Type, arg1: $Function$Type<($SourceFile$Type), (T)>)

public "apply"(arg0: $SourceFile$Type): T
public "getVersion"(): $GLSLVersion
get "version"(): $GLSLVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Template$Type<T> = ($Template<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Template_<T> = $Template$Type<(T)>;
}}
