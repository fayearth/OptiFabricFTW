package me.modmuss50.optifabric.compat.carpet.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.WorldRenderer;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("carpet/mixins/WorldRenderer_pausedShakeMixin")
abstract class WorldRendererExtraMixin {
	@ModifyVariable(method = "render", argsOnly = true, ordinal = 0, require = 2,
					at = @At(value = "INVOKE", 
								target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/render/Frustum;)V",
								shift = At.Shift.BEFORE))
	private float doChangeTickPhaseBack(float previous) {
		return changeTickPhaseBack(previous);
	}

	@Shim
    private native float changeTickPhaseBack(float previous);
}