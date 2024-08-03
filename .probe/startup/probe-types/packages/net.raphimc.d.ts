declare module "packages/net/raphimc/immediatelyfast/feature/batching/$RenderSystemState" {
import {$BlendFuncDepthFuncState, $BlendFuncDepthFuncState$Type} from "packages/net/raphimc/immediatelyfast/feature/batching/$BlendFuncDepthFuncState"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"

export class $RenderSystemState extends $Record {

constructor(texture0: integer, texture1: integer, texture2: integer, program: $ShaderInstance$Type, shaderColor: (float)[], blendFuncDepthFunc: $BlendFuncDepthFuncState$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "apply"(): void
public static "current"(): $RenderSystemState
public "blendFuncDepthFunc"(): $BlendFuncDepthFuncState
public "shaderColor"(): (float)[]
public "texture1"(): integer
public "texture0"(): integer
public "texture2"(): integer
public "program"(): $ShaderInstance
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSystemState$Type = ($RenderSystemState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSystemState_ = $RenderSystemState$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/$ImmediatelyFast" {
import {$ImmediatelyFastRuntimeConfig, $ImmediatelyFastRuntimeConfig$Type} from "packages/net/raphimc/immediatelyfast/feature/core/$ImmediatelyFastRuntimeConfig"
import {$ImmediatelyFastConfig, $ImmediatelyFastConfig$Type} from "packages/net/raphimc/immediatelyfast/feature/core/$ImmediatelyFastConfig"
import {$PersistentMappedStreamingBuffer, $PersistentMappedStreamingBuffer$Type} from "packages/net/raphimc/immediatelyfast/feature/fast_buffer_upload/$PersistentMappedStreamingBuffer"
import {$Unsafe, $Unsafe$Type} from "packages/sun/misc/$Unsafe"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$SignTextCache, $SignTextCache$Type} from "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignTextCache"

export class $ImmediatelyFast {
static readonly "LOGGER": $Logger
static readonly "UNSAFE": $Unsafe
static "VERSION": string
static "config": $ImmediatelyFastConfig
static "runtimeConfig": $ImmediatelyFastRuntimeConfig
static "persistentMappedStreamingBuffer": $PersistentMappedStreamingBuffer
static "signTextCache": $SignTextCache

constructor()

public static "createRuntimeConfig"(): void
public static "loadConfig"(): void
public static "earlyInit"(): void
public static "windowInit"(): void
public static "onWorldJoin"(): void
public static "lateInit"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmediatelyFast$Type = ($ImmediatelyFast);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmediatelyFast_ = $ImmediatelyFast$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignTextCache" {
import {$SignAtlasFramebuffer$Slot, $SignAtlasFramebuffer$Slot$Type} from "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignAtlasFramebuffer$Slot"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$SignText, $SignText$Type} from "packages/net/minecraft/world/level/block/entity/$SignText"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$SignAtlasFramebuffer, $SignAtlasFramebuffer$Type} from "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignAtlasFramebuffer"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $SignTextCache implements $ResourceManagerReloadListener {
readonly "signAtlasFramebuffer": $SignAtlasFramebuffer
readonly "slotCache": $Cache<($SignText), ($SignAtlasFramebuffer$Slot)>
readonly "renderLayer": $RenderType

constructor()

public "clearCache"(): void
public "onResourceManagerReload"(manager: $ResourceManager$Type): void
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SignTextCache$Type = ($SignTextCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SignTextCache_ = $SignTextCache$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$GuiOverlayFirstBatchingBuffer" {
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$BatchingBuffer, $BatchingBuffer$Type} from "packages/net/raphimc/immediatelyfast/feature/batching/$BatchingBuffer"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GuiOverlayFirstBatchingBuffer extends $BatchingBuffer {
static "IS_DRAWING": boolean

constructor()

public "endBatch"(): void
public static "immediateWithBuffers"(layerBuffers: $Map$Type<(any), (any)>, fallbackBuffer: $BufferBuilder$Type): $MultiBufferSource$BufferSource
public static "immediate"(arg0: $BufferBuilder$Type): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiOverlayFirstBatchingBuffer$Type = ($GuiOverlayFirstBatchingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiOverlayFirstBatchingBuffer_ = $GuiOverlayFirstBatchingBuffer$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$NoSetTextAnglesMatrixStack" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$TransformStack, $TransformStack$Type} from "packages/com/jozufozu/flywheel/util/transform/$TransformStack"

export class $NoSetTextAnglesMatrixStack extends $PoseStack {

constructor()

public static "cast"(arg0: $PoseStack$Type): $TransformStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoSetTextAnglesMatrixStack$Type = ($NoSetTextAnglesMatrixStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoSetTextAnglesMatrixStack_ = $NoSetTextAnglesMatrixStack$Type;
}}
declare module "packages/net/raphimc/immediatelyfastapi/$ApiAccess" {
import {$ConfigAccess, $ConfigAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$ConfigAccess"
import {$BatchingAccess, $BatchingAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$BatchingAccess"

export interface $ApiAccess {

 "getConfig"(): $ConfigAccess
 "getRuntimeConfig"(): $ConfigAccess
 "getBatching"(): $BatchingAccess
}

export namespace $ApiAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ApiAccess$Type = ($ApiAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ApiAccess_ = $ApiAccess$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/$PlatformCode" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $PlatformCode {

constructor()

public static "checkModCompatibility"(): void
public static "getModVersion"(mod: string): $Optional<(string)>
public static "getConfigDirectory"(): $Path
get "configDirectory"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformCode$Type = ($PlatformCode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformCode_ = $PlatformCode$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/processors/$InjectAboveEverythingProcessor" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"

export class $InjectAboveEverythingProcessor {

constructor()

public static "process"(classNode: $ClassNode$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectAboveEverythingProcessor$Type = ($InjectAboveEverythingProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectAboveEverythingProcessor_ = $InjectAboveEverythingProcessor$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/interfaces/$IMapRenderer" {
import {$MapAtlasTexture, $MapAtlasTexture$Type} from "packages/net/raphimc/immediatelyfast/feature/map_atlas_generation/$MapAtlasTexture"

export interface $IMapRenderer {

 "immediatelyFast$getAtlasMapping"(arg0: integer): integer
 "immediatelyFast$getMapAtlasTexture"(arg0: integer): $MapAtlasTexture
}

export namespace $IMapRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMapRenderer$Type = ($IMapRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMapRenderer_ = $IMapRenderer$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/interfaces/$ISignText" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ISignText {

 "immediatelyFast$setShouldCache"(arg0: boolean): void
 "immediatelyFast$shouldCache"(): boolean
}

export namespace $ISignText {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISignText$Type = ($ISignText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISignText_ = $ISignText$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/processors/$InjectOnAllReturns" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $InjectOnAllReturns extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $InjectOnAllReturns {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectOnAllReturns$Type = ($InjectOnAllReturns);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectOnAllReturns_ = $InjectOnAllReturns$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/apiimpl/$BatchingAccessImpl" {
import {$BatchingAccess, $BatchingAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$BatchingAccess"

export class $BatchingAccessImpl implements $BatchingAccess {

constructor()

public "hasDataToDraw"(): boolean
public "isHudBatching"(): boolean
public "forceDrawBuffers"(): void
public "beginHudBatching"(): void
public "endHudBatching"(): void
get "hudBatching"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatchingAccessImpl$Type = ($BatchingAccessImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatchingAccessImpl_ = $BatchingAccessImpl$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$LightingState" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"

export class $LightingState extends $Record {

constructor(shaderLightDirection0: $Vector3f$Type, shaderLightDirection1: $Vector3f$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "apply"(): void
public static "current"(): $LightingState
public "saveAndApply"(): void
public "shaderLightDirection1"(): $Vector3f
public "shaderLightDirection0"(): $Vector3f
public "revert"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightingState$Type = ($LightingState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightingState_ = $LightingState$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/compat/$TriConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TriConsumer<A, B, C> {

 "accept"(arg0: A, arg1: B, arg2: C): void

(arg0: A, arg1: B, arg2: C): void
}

export namespace $TriConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TriConsumer$Type<A, B, C> = ($TriConsumer<(A), (B), (C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TriConsumer_<A, B, C> = $TriConsumer$Type<(A), (B), (C)>;
}}
declare module "packages/net/raphimc/immediatelyfast/compat/$CoreShaderBlacklist" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $CoreShaderBlacklist {

constructor()

public static "getBlacklist"(): $List<(string)>
public static "isBlacklisted"(name: string): boolean
get "blacklist"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreShaderBlacklist$Type = ($CoreShaderBlacklist);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreShaderBlacklist_ = $CoreShaderBlacklist$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/apiimpl/$ApiAccessImpl" {
import {$ConfigAccess, $ConfigAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$ConfigAccess"
import {$ApiAccess, $ApiAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$ApiAccess"
import {$BatchingAccess, $BatchingAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$BatchingAccess"

export class $ApiAccessImpl implements $ApiAccess {

constructor()

public "getConfig"(): $ConfigAccess
public "getRuntimeConfig"(): $ConfigAccess
public "getBatching"(): $BatchingAccess
get "config"(): $ConfigAccess
get "runtimeConfig"(): $ConfigAccess
get "batching"(): $BatchingAccess
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ApiAccessImpl$Type = ($ApiAccessImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ApiAccessImpl_ = $ApiAccessImpl$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/fast_buffer_upload/$PersistentMappedStreamingBuffer" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $PersistentMappedStreamingBuffer {

constructor(size: long)

public "flush"(): void
public "getSize"(): long
public "getOffset"(): long
public "addUpload"(destinationId: integer, data: $ByteBuffer$Type): void
get "size"(): long
get "offset"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentMappedStreamingBuffer$Type = ($PersistentMappedStreamingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentMappedStreamingBuffer_ = $PersistentMappedStreamingBuffer$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/apiimpl/$RuntimeConfigAccessImpl" {
import {$ConfigAccess, $ConfigAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$ConfigAccess"

export class $RuntimeConfigAccessImpl implements $ConfigAccess {

constructor()

public "getBoolean"(key: string, defaultValue: boolean): boolean
public "getInt"(key: string, defaultValue: integer): integer
public "getLong"(key: string, defaultValue: long): long
public "getString"(key: string, defaultValue: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimeConfigAccessImpl$Type = ($RuntimeConfigAccessImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimeConfigAccessImpl_ = $RuntimeConfigAccessImpl$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/$ImmediatelyFastMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ImmediatelyFastMixinPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(mixinPackage: string): void
public "postApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(targetClassName: string, mixinClassName: string): boolean
public "preApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "acceptTargets"(myTargets: $Set$Type<(string)>, otherTargets: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmediatelyFastMixinPlugin$Type = ($ImmediatelyFastMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmediatelyFastMixinPlugin_ = $ImmediatelyFastMixinPlugin$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$ItemModelBatchingBuffer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$BatchingBuffer, $BatchingBuffer$Type} from "packages/net/raphimc/immediatelyfast/feature/batching/$BatchingBuffer"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemModelBatchingBuffer extends $BatchingBuffer {
static "IS_DRAWING": boolean

constructor()

public "close"(): void
public "endBatch"(): void
public "getBuffer"(layer: $RenderType$Type): $VertexConsumer
public "endBatch"(layer: $RenderType$Type): void
public static "immediateWithBuffers"(layerBuffers: $Map$Type<(any), (any)>, fallbackBuffer: $BufferBuilder$Type): $MultiBufferSource$BufferSource
public static "immediate"(arg0: $BufferBuilder$Type): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelBatchingBuffer$Type = ($ItemModelBatchingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelBatchingBuffer_ = $ItemModelBatchingBuffer$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$BatchingBuffer" {
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$BatchableBufferSource, $BatchableBufferSource$Type} from "packages/net/raphimc/immediatelyfast/feature/core/$BatchableBufferSource"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BatchingBuffer extends $BatchableBufferSource {
static "IS_DRAWING": boolean

constructor(fallbackBuffer: $BufferBuilder$Type, layerBuffers: $Map$Type<($RenderType$Type), ($BufferBuilder$Type)>)
constructor(layerBuffers: $Map$Type<($RenderType$Type), ($BufferBuilder$Type)>)
constructor()

public "endBatch"(layer: $RenderType$Type): void
public static "immediateWithBuffers"(layerBuffers: $Map$Type<(any), (any)>, fallbackBuffer: $BufferBuilder$Type): $MultiBufferSource$BufferSource
public static "immediate"(arg0: $BufferBuilder$Type): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatchingBuffer$Type = ($BatchingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatchingBuffer_ = $BatchingBuffer$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/core/$BatchableBufferSource" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BatchableBufferSource extends $MultiBufferSource$BufferSource implements $AutoCloseable {

constructor(fallbackBuffer: $BufferBuilder$Type, layerBuffers: $Map$Type<($RenderType$Type), ($BufferBuilder$Type)>)
constructor(layerBuffers: $Map$Type<($RenderType$Type), ($BufferBuilder$Type)>)
constructor()

public "close"(): void
public "endBatch"(): void
public "getBuffer"(layer: $RenderType$Type): $VertexConsumer
public "endBatch"(layer: $RenderType$Type): void
public "endLastBatch"(): void
public "hasActiveLayers"(): boolean
public static "immediateWithBuffers"(layerBuffers: $Map$Type<(any), (any)>, fallbackBuffer: $BufferBuilder$Type): $MultiBufferSource$BufferSource
public static "immediate"(arg0: $BufferBuilder$Type): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatchableBufferSource$Type = ($BatchableBufferSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatchableBufferSource_ = $BatchableBufferSource$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$BatchingBuffers" {
import {$BatchingBuffer, $BatchingBuffer$Type} from "packages/net/raphimc/immediatelyfast/feature/batching/$BatchingBuffer"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BatchingBuffers {
static "FILL_CONSUMER": $MultiBufferSource
static "TEXTURE_CONSUMER": $MultiBufferSource
static "TEXT_CONSUMER": $MultiBufferSource
static "ITEM_MODEL_CONSUMER": $MultiBufferSource
static "ITEM_OVERLAY_CONSUMER": $MultiBufferSource

constructor()

public static "createLayerBuffers"(...layers: ($RenderType$Type)[]): $Map<($RenderType), ($BufferBuilder)>
public static "hasDataToDraw"(): boolean
public static "isHudBatching"(): boolean
public static "forceDrawBuffers"(): void
public static "beginHudBatching"(batch: $BatchingBuffer$Type): void
public static "beginHudBatching"(): void
public static "endHudBatching"(batch: $BatchingBuffer$Type): void
public static "endHudBatching"(): void
public static "beginDebugHudBatching"(): void
public static "endItemOverlayRendering"(): void
public static "endDebugHudBatching"(): void
public static "beginItemOverlayRendering"(): void
get "hudBatching"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatchingBuffers$Type = ($BatchingBuffers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatchingBuffers_ = $BatchingBuffers$Type;
}}
declare module "packages/net/raphimc/immediatelyfastapi/$BatchingAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BatchingAccess {

 "hasDataToDraw"(): boolean
 "isHudBatching"(): boolean
 "forceDrawBuffers"(): void
 "beginHudBatching"(): void
 "endHudBatching"(): void
}

export namespace $BatchingAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatchingAccess$Type = ($BatchingAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatchingAccess_ = $BatchingAccess$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/forge/$ImmediatelyFastForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ImmediatelyFastForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmediatelyFastForge$Type = ($ImmediatelyFastForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmediatelyFastForge_ = $ImmediatelyFastForge$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/core/$BufferBuilderPool" {
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"

export class $BufferBuilderPool {


public static "get"(): $BufferBuilder
public static "getAllocatedSize"(): integer
get "allocatedSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferBuilderPool$Type = ($BufferBuilderPool);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferBuilderPool_ = $BufferBuilderPool$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/compat/$IrisCompat" {
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$BooleanConsumer, $BooleanConsumer$Type} from "packages/it/unimi/dsi/fastutil/booleans/$BooleanConsumer"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$TriConsumer, $TriConsumer$Type} from "packages/net/raphimc/immediatelyfast/compat/$TriConsumer"

export class $IrisCompat {
static "IRIS_LOADED": boolean
static "isRenderingLevel": $BooleanSupplier
static "renderWithExtendedVertexFormat": $BooleanConsumer
static "iris$beginWithoutExtending": $TriConsumer<($BufferBuilder), ($VertexFormat$Mode), ($VertexFormat)>

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IrisCompat$Type = ($IrisCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IrisCompat_ = $IrisCompat$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/processors/$InjectOnAllReturnsProcessor" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"

export class $InjectOnAllReturnsProcessor {

constructor()

public static "process"(classNode: $ClassNode$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectOnAllReturnsProcessor$Type = ($InjectOnAllReturnsProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectOnAllReturnsProcessor_ = $InjectOnAllReturnsProcessor$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/interfaces/$IBufferBuilder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IBufferBuilder {

 "immediatelyFast$isReleased"(): boolean
 "immediatelyFast$release"(): void
}

export namespace $IBufferBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBufferBuilder$Type = ($IBufferBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBufferBuilder_ = $IBufferBuilder$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignAtlasFramebuffer" {
import {$SignAtlasFramebuffer$Slot, $SignAtlasFramebuffer$Slot$Type} from "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignAtlasFramebuffer$Slot"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderTarget, $RenderTarget$Type} from "packages/com/mojang/blaze3d/pipeline/$RenderTarget"

export class $SignAtlasFramebuffer extends $RenderTarget implements $AutoCloseable {
static readonly "ATLAS_SIZE": integer
 "width": integer
 "height": integer
 "viewWidth": integer
 "viewHeight": integer
readonly "useDepth": boolean
 "frameBufferId": integer
 "filterMode": integer

constructor()

public "clear"(): void
public "close"(): void
public "getTextureId"(): $ResourceLocation
public "findSlot"(width: integer, height: integer): $SignAtlasFramebuffer$Slot
get "textureId"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SignAtlasFramebuffer$Type = ($SignAtlasFramebuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SignAtlasFramebuffer_ = $SignAtlasFramebuffer$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/map_atlas_generation/$MapAtlasTexture" {
import {$CallbackInfo, $CallbackInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfo"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DynamicTexture, $DynamicTexture$Type} from "packages/net/minecraft/client/renderer/texture/$DynamicTexture"

export class $MapAtlasTexture implements $AutoCloseable {
static readonly "ATLAS_SIZE": integer
static readonly "MAP_SIZE": integer
static readonly "MAPS_PER_ATLAS": integer

constructor(id: integer)

public "getId"(): integer
public "close"(): void
public "getIdentifier"(): $ResourceLocation
public "getTexture"(): $DynamicTexture
public "getNextMapLocation"(): integer
public "handler$bdi000$forceMipMapOff"(id: integer, ci: $CallbackInfo$Type): void
get "id"(): integer
get "identifier"(): $ResourceLocation
get "texture"(): $DynamicTexture
get "nextMapLocation"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapAtlasTexture$Type = ($MapAtlasTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapAtlasTexture_ = $MapAtlasTexture$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/core/$ImmediatelyFastRuntimeConfig" {
import {$ImmediatelyFastConfig, $ImmediatelyFastConfig$Type} from "packages/net/raphimc/immediatelyfast/feature/core/$ImmediatelyFastConfig"

export class $ImmediatelyFastRuntimeConfig {
 "hud_batching": boolean
 "font_atlas_resizing": boolean
 "fast_buffer_upload": boolean
 "legacy_fast_buffer_upload": boolean

constructor(config: $ImmediatelyFastConfig$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmediatelyFastRuntimeConfig$Type = ($ImmediatelyFastRuntimeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmediatelyFastRuntimeConfig_ = $ImmediatelyFastRuntimeConfig$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/forge/$PlatformCodeImpl" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $PlatformCodeImpl {

constructor()

public static "checkModCompatibility"(): void
public static "getModVersion"(mod: string): $Optional<(string)>
public static "getConfigDirectory"(): $Path
get "configDirectory"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformCodeImpl$Type = ($PlatformCodeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformCodeImpl_ = $PlatformCodeImpl$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignAtlasFramebuffer$Slot" {
import {$SignAtlasFramebuffer, $SignAtlasFramebuffer$Type} from "packages/net/raphimc/immediatelyfast/feature/sign_text_buffering/$SignAtlasFramebuffer"

export class $SignAtlasFramebuffer$Slot {
readonly "x": integer
readonly "y": integer
readonly "width": integer
readonly "height": integer
readonly "parentSlot": $SignAtlasFramebuffer$Slot
 "subSlot1": $SignAtlasFramebuffer$Slot
 "subSlot2": $SignAtlasFramebuffer$Slot
 "occupied": boolean

constructor(this$0: $SignAtlasFramebuffer$Type, parentSlot: $SignAtlasFramebuffer$Slot$Type, x: integer, y: integer, width: integer, height: integer)

public "findSlot"(width: integer, height: integer): $SignAtlasFramebuffer$Slot
public "markFree"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SignAtlasFramebuffer$Slot$Type = ($SignAtlasFramebuffer$Slot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SignAtlasFramebuffer$Slot_ = $SignAtlasFramebuffer$Slot$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$BlendFuncDepthFuncState" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $BlendFuncDepthFuncState extends $Record {

constructor(BLEND: boolean, DEPTH_TEST: boolean, GL_BLEND_SRC_RGB: integer, GL_BLEND_SRC_ALPHA: integer, GL_BLEND_DST_RGB: integer, GL_BLEND_DST_ALPHA: integer, GL_DEPTH_FUNC: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "apply"(): void
public static "current"(): $BlendFuncDepthFuncState
public "saveAndApply"(): void
public "DEPTH_TEST"(): boolean
public "GL_DEPTH_FUNC"(): integer
public "GL_BLEND_SRC_RGB"(): integer
public "GL_BLEND_SRC_ALPHA"(): integer
public "GL_BLEND_DST_RGB"(): integer
public "GL_BLEND_DST_ALPHA"(): integer
public "BLEND"(): boolean
public "revert"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlendFuncDepthFuncState$Type = ($BlendFuncDepthFuncState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlendFuncDepthFuncState_ = $BlendFuncDepthFuncState$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/core/$ImmediatelyFastConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ImmediatelyFastConfig {
 "font_atlas_resizing": boolean
 "map_atlas_generation": boolean
 "hud_batching": boolean
 "fast_text_lookup": boolean
 "fast_buffer_upload": boolean
 "fast_buffer_upload_size_mb": long
 "fast_buffer_upload_explicit_flush": boolean
 "dont_add_info_into_debug_hud": boolean
 "experimental_disable_error_checking": boolean
 "experimental_disable_resource_pack_conflict_handling": boolean
 "experimental_sign_text_buffering": boolean
 "experimental_screen_batching": boolean
 "debug_only_and_not_recommended_disable_universal_batching": boolean
 "debug_only_and_not_recommended_disable_mod_conflict_handling": boolean
 "debug_only_and_not_recommended_disable_hardware_conflict_handling": boolean
 "debug_only_print_additional_error_information": boolean
 "debug_only_use_last_usage_for_batch_ordering": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmediatelyFastConfig$Type = ($ImmediatelyFastConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmediatelyFastConfig_ = $ImmediatelyFastConfig$Type;
}}
declare module "packages/net/raphimc/immediatelyfastapi/$ConfigAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ConfigAccess {

 "getBoolean"(arg0: string, arg1: boolean): boolean
 "getInt"(arg0: string, arg1: integer): integer
 "getLong"(arg0: string, arg1: long): long
 "getString"(arg0: string, arg1: string): string
}

export namespace $ConfigAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigAccess$Type = ($ConfigAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigAccess_ = $ConfigAccess$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/feature/batching/$BatchingRenderLayers" {
import {$BlendFuncDepthFuncState, $BlendFuncDepthFuncState$Type} from "packages/net/raphimc/immediatelyfast/feature/batching/$BlendFuncDepthFuncState"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BatchingRenderLayers {
static readonly "COLORED_TEXTURE": $BiFunction<(integer), ($BlendFuncDepthFuncState), ($RenderType)>

constructor()

public static "memoizeTemp"<A>(arg0: $Function$Type<(A), ($RenderType$Type)>): $Function<(A), ($RenderType)>
public static "memoizeTemp"<A, B>(arg0: $BiFunction$Type<(A), (B), ($RenderType$Type)>): $BiFunction<(A), (B), ($RenderType)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatchingRenderLayers$Type = ($BatchingRenderLayers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatchingRenderLayers_ = $BatchingRenderLayers$Type;
}}
declare module "packages/net/raphimc/immediatelyfastapi/$ImmediatelyFastApi" {
import {$ApiAccess, $ApiAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$ApiAccess"

export class $ImmediatelyFastApi {

constructor()

public static "setApiImpl"(impl: $ApiAccess$Type): void
public static "getApiImpl"(): $ApiAccess
set "apiImpl"(value: $ApiAccess$Type)
get "apiImpl"(): $ApiAccess
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmediatelyFastApi$Type = ($ImmediatelyFastApi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmediatelyFastApi_ = $ImmediatelyFastApi$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/injection/processors/$InjectAboveEverything" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $InjectAboveEverything extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $InjectAboveEverything {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectAboveEverything$Type = ($InjectAboveEverything);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectAboveEverything_ = $InjectAboveEverything$Type;
}}
declare module "packages/net/raphimc/immediatelyfast/apiimpl/$ConfigAccessImpl" {
import {$ConfigAccess, $ConfigAccess$Type} from "packages/net/raphimc/immediatelyfastapi/$ConfigAccess"

export class $ConfigAccessImpl implements $ConfigAccess {

constructor()

public "getBoolean"(key: string, defaultValue: boolean): boolean
public "getInt"(key: string, defaultValue: integer): integer
public "getLong"(key: string, defaultValue: long): long
public "getString"(key: string, defaultValue: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigAccessImpl$Type = ($ConfigAccessImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigAccessImpl_ = $ConfigAccessImpl$Type;
}}
