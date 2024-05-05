package me.retucio.retclient.mixin;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.manager.ModuleManager;

//@Mixin( RenderTickCounter.class )
//public class RenderTickCounterMixin {
//	
//    @Shadow public float lastFrameDuration;
//
//    @Inject(method = "beginRenderTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J"))
//    public void beginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
//        this.lastFrameDuration *= RetClient.TIMER;
//    }
//} <-- original code (just in case yk)

@Mixin(RenderTickCounter.class)
public class RenderTickCounterMixin {

	@Shadow private float lastFrameDuration;
	@Shadow private float tickDelta;
	@Shadow private long prevTimeMillis;
	@Shadow private float tickTime;

	@Inject(method = "beginRenderTick", at = @At("HEAD"), cancellable = true)
	private void beginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> ci) {
		if (ModuleManager.timer.isEnabled()) {
			
			this.lastFrameDuration = (float) (((timeMillis - this.prevTimeMillis) / this.tickTime)
					* ModuleManager.timer.tps.getValue());
			this.prevTimeMillis = timeMillis;
			this.tickDelta += this.lastFrameDuration;
			int i = (int) this.tickDelta;
			this.tickDelta -= i;

			ci.setReturnValue(i);
		}
	}

}