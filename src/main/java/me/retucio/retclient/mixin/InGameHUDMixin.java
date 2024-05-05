package me.retucio.retclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;

import me.retucio.retclient.event.events.Render2DEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

import static me.retucio.retclient.util.traits.Util.EVENT_BUS;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( InGameHud.class )
public class InGameHUDMixin {

    @SuppressWarnings("resource")
	@Inject(method = "render", at = @At("RETURN"))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().inGameHud.getDebugHud().shouldShowDebugHud()) return;
        
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableCull();
        
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        Render2DEvent event = new Render2DEvent(context, tickDelta);
        EVENT_BUS.post(event);

        RenderSystem.enableDepthTest();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}