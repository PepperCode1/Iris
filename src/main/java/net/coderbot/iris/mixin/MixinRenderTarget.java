package net.coderbot.iris.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.coderbot.iris.rendertarget.Blaze3dRenderTargetExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows Iris to detect when the depth texture was re-created, so we can re-attach it
 * to the shader framebuffers. See DeferredWorldRenderingPipeline and RenderTargets.
 */
@Mixin(RenderTarget.class)
public class MixinRenderTarget implements Blaze3dRenderTargetExt {
	private boolean iris$depthDirty = false;
	private boolean iris$colorDirty = false;

	@Inject(method = "destroyBuffers()V", at = @At("HEAD"))
	private void iris$onDestroyBuffers(CallbackInfo ci) {
		iris$depthDirty = true;
		iris$colorDirty = true;
	}

	@Override
	public boolean iris$isDepthBufferDirty() {
		return iris$depthDirty;
	}

	@Override
	public void iris$clearDepthBufferDirtyFlag() {
		iris$depthDirty = false;
	}

	@Override
	public boolean iris$isColorBufferDirty() {
		return iris$colorDirty;
	}

	@Override
	public void iris$clearColorBufferDirtyFlag() {
		iris$colorDirty = false;
	}
}
