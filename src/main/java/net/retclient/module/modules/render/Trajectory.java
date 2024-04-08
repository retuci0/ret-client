package net.retclient.module.modules.render;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.systems.RenderSystem;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.gui.Color;
import net.retclient.misc.ModuleUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class Trajectory extends Module implements RenderListener {

	private ColorSetting color = new ColorSetting("trajectory_color", "Color", "Color", new Color(0, 1f, 1f));
	
	public Trajectory() {
		super(new KeybindSetting("key.trajectory", "Trajectory Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Trajectory");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see where they are aiming. (DISABLED)");
		
		this.addSetting(color);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(RenderListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	@Override
	public void OnRender(RenderEvent event) {
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = event.GetMatrixStack();
		matrixStack.push();
		
		RenderSystem.setShaderColor(0, 0, 0, 1);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		
		ItemStack itemStack = mc.player.getActiveItem();

		if(!(ModuleUtils.isThrowable(itemStack))) return;
		
		float initialVelocity = (52 * BowItem.getPullProgress(mc.player.getItemUseTime()));
		Vec3d prevPoint = new Vec3d(0,0,0);
		
		bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION);
		for(int iteration = 0; iteration < 1000; iteration++){
			bufferBuilder.vertex(matrix, (float) prevPoint.x, (float) prevPoint.y, (float) prevPoint.z).next();
			
			float distance =  (float) ((initialVelocity)*Math.sin(2*mc.player.getRotationVector().x) / 9.0f);
			Vec3d nextPoint = mc.player.getRotationVector().multiply(distance);
			bufferBuilder.vertex(matrix, (float) nextPoint.x, (float) nextPoint.y, (float) nextPoint.z).next();
			
			prevPoint = nextPoint;
		}
		

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		
		matrixStack.pop();
	}

}