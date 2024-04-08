package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.retclient.Main;
import net.retclient.Client;
import net.retclient.module.modules.render.XRay;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

@Mixin(TerrainRenderContext.class)
public class TerrainRenderContextMixin {
	@Inject(at = { @At("HEAD") }, method = { "tessellateBlock" }, cancellable = true, remap = false)
	private void tesselateBlock(BlockState blockState, BlockPos blockPos, final BakedModel model,
			MatrixStack matrixStack, CallbackInfo ci) {
		Client ret = Main.getInstance();
		XRay xray = (XRay)ret.moduleManager.xray;
		if (xray.getState()) {
			if (xray.isXRayBlock(blockState.getBlock())) {
				ci.cancel();
				return;
			}
		}
	}
}