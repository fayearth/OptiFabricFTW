package me.modmuss50.optifabric.compat.frex.mixin.old;

import java.util.Random;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.LoudCoerce;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import me.modmuss50.optifabric.compat.frex.BridgedChunkRendererRegion;
import me.modmuss50.optifabric.compat.frex.mixin.ChunkCacheOF;

@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder$BuiltChunk$RebuildTask")
@InterceptingMixin("grondag/frex/mixin/client/render/chunk/ChunkBuilderMixin")
abstract class RebuildTaskMixin {
	@PlacatingSurrogate
	private void initializeContext(float cameraX, float cameraY, float cameraZ, ChunkData data, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<Set<BlockEntity>> call,
			int one, BlockPos origin, BlockPos edge, ChunkOcclusionDataBuilder occlusionDataBuilder, Set<BlockEntity> bes, MatrixStack matrices,
			@LoudCoerce(value = "net/optifine/override/ChunkCacheOF", remap = false) Object chunkCache, RenderLayer[] singleLayer, Random random) {
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/ChunkBuilder$ChunkData;Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/Set;",
			at = @At(value = "INVOKE", target = "Lnet/optifine/BlockPosM;getAllInBoxMutable(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Iterable;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void onOFRender(float cameraX, float cameraY, float cameraZ, ChunkData data, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<Set<BlockEntity>> call,
			int one, BlockPos origin, BlockPos edge, ChunkOcclusionDataBuilder occlusionDataBuilder, Set<BlockEntity> bes, MatrixStack matrices,
			@Coerce ChunkCacheOF chunkCache, RenderLayer[] singleLayer, Random random, BlockRenderManager blockRenderManager) {
		initializeContext(cameraX, cameraY, cameraZ, data, buffers, call, one, origin, edge, occlusionDataBuilder, bes, new BridgedChunkRendererRegion(chunkCache), matrices, random, blockRenderManager);
	}

	@Shim
	private native void initializeContext(float cameraX, float cameraY, float cameraZ, ChunkData data, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<Set<BlockEntity>> call,
			int i, BlockPos blockPos, BlockPos blockPos2, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, Set<BlockEntity> set, ChunkRendererRegion chunkRendererRegion,
			MatrixStack matrixStack, Random random, BlockRenderManager blockRenderManager);
}