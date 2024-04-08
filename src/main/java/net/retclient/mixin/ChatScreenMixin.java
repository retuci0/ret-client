package net.retclient.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.retclient.Main;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.GlobalChat;
import net.retclient.cmd.GlobalChat.ChatType;
import net.retclient.gui.Color;
import net.retclient.gui.tabs.components.ButtonComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends ScreenMixin{
	@Shadow
	protected TextFieldWidget chatField;
	
	protected ButtonComponent serverChatButton;
	protected ButtonComponent globalChatButton;
	
	@Inject(at = { @At("TAIL") }, method = {"init()V" }, cancellable = true)
	public void onInit(CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		int guiScale = mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
		
		// Create server chat button.
		serverChatButton = new ButtonComponent(null, "Server Chat", new Runnable() {
			@Override
			public void run() {
				GlobalChat.chatType = GlobalChat.ChatType.Minecraft;
				serverChatButton.setBackgroundColor(new Color(56, 56, 56));
				globalChatButton.setBackgroundColor(new Color(128, 128, 128));
			}
		}, new Color(192, 192, 192), new Color(56, 56, 56));
		serverChatButton.setX(chatField.getX() * guiScale);
		serverChatButton.setY((chatField.getY() - chatField.getHeight() - 10) * guiScale);
		serverChatButton.setWidth(140);
		serverChatButton.setHeight(30);
		
		// Create global chat button
		globalChatButton = new ButtonComponent(null, "Global Chat", new Runnable() {
			@Override
			public void run() {
				GlobalChat.chatType = GlobalChat.ChatType.Global;
				globalChatButton.setBackgroundColor(new Color(56, 56, 56));
				serverChatButton.setBackgroundColor(new Color(128, 128, 128));
			}
		}, new Color(192, 192, 192), new Color(128, 128, 128));
		globalChatButton.setX((chatField.getX() + 80) * guiScale);
		globalChatButton.setY((chatField.getY() - chatField.getHeight() - 10) * guiScale);
		globalChatButton.setWidth(140);
		globalChatButton.setHeight(30);
		
		serverChatButton.setVisible(true);
		globalChatButton.setVisible(true);
	}

	@Override
	protected void onClose(CallbackInfo ci) {
		serverChatButton.setVisible(false);
		globalChatButton.setVisible(false);
	}
	
	@Override
	protected void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		super.onRender(context, mouseX, mouseY, delta, ci);
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = context.getMatrices();
		matrixStack.push();
		
		int guiScale = mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
		matrixStack.scale(1.0f / guiScale, 1.0f / guiScale, 1.0f);
		serverChatButton.draw(context, delta);
		globalChatButton.draw(context, delta);
		matrixStack.pop();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	@Inject(at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addToMessageHistory(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.AFTER) }, method = "sendMessage(Ljava/lang/String;Z)Z", cancellable = true)
	public void onSendMessage(String message, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
		if (message.startsWith(CommandManager.PREFIX.getValue())) {
			Main.getInstance().commandManager.command(message.split(" "));
			cir.setReturnValue(true);
		}else if (GlobalChat.chatType == ChatType.Global) {
			Main.getInstance().globalChat.SendMessage(message);
			cir.setReturnValue(true);
		}
	}
}