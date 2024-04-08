package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;

public class Sneak extends Module implements TickListener {

	public Sneak() {
		super(new KeybindSetting("key.sneakhack", "Sneak Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Sneak");
		this.setCategory(Category.Movement);
		this.setDescription("Makes the player appear like they're sneaking.");
	}

	@Override
	public void onDisable() {
		ClientPlayerEntity player = MC.player;
		if(player != null && player.networkHandler != null) {
			player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, Mode.RELEASE_SHIFT_KEY));
		}
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnUpdate(TickEvent event) {
		ClientPlayerEntity player = MC.player;
		MC.player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, Mode.PRESS_SHIFT_KEY));
		MC.player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, Mode.RELEASE_SHIFT_KEY));
	}
}