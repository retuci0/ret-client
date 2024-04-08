package net.retclient.module.modules.misc;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.ReceivePacketEvent;
import net.retclient.event.listeners.ReceivePacketListener;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class AutoFish extends Module implements ReceivePacketListener {
	public AutoFish() {
		super(new KeybindSetting("key.autofish", "AutoFish Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AutoFish");
		this.setCategory(Category.Misc);
		this.setDescription("Automatically fishes for you.");
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(ReceivePacketListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(ReceivePacketListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	private void recastRod() {
		
		PlayerInteractItemC2SPacket packetTryUse = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0);
		MC.player.networkHandler.sendPacket(packetTryUse);
		MC.player.networkHandler.sendPacket(packetTryUse);
	}

	@Override
	public void OnReceivePacket(ReceivePacketEvent readPacketEvent) {
		Packet<?> packet = readPacketEvent.GetPacket();
		
		if(packet instanceof PlaySoundS2CPacket ) {
			PlaySoundS2CPacket soundPacket = (PlaySoundS2CPacket)packet;
			if(soundPacket.getSound().value().equals(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH)) {
				recastRod();
			}
		}
	}

}
