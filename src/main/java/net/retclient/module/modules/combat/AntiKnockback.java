package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.ReceivePacketEvent;
import net.retclient.event.listeners.ReceivePacketListener;
import net.retclient.mixin.interfaces.IEntityVelocityUpdateS2CPacket;
import net.retclient.mixin.interfaces.IExplosionS2CPacket;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class AntiKnockback extends Module implements ReceivePacketListener {
	
	public AntiKnockback() {
		super(new KeybindSetting("key.antiknockback", "AntiKnockback Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("AntiKnockback");
		
		this.setCategory(Category.Combat);
		this.setDescription("Prevents knockback.");
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

	@Override
	public void OnReceivePacket(ReceivePacketEvent readPacketEvent) {
		MinecraftClient mc = MinecraftClient.getInstance();
		Packet<?> packet = readPacketEvent.GetPacket();
		
		if(packet instanceof EntityVelocityUpdateS2CPacket) {
			IEntityVelocityUpdateS2CPacket velocityUpdatePacket = (IEntityVelocityUpdateS2CPacket) packet;
			if(velocityUpdatePacket.getId() == mc.player.getId()) {
				velocityUpdatePacket.setVelocityX(0);
				velocityUpdatePacket.setVelocityY(0);
				velocityUpdatePacket.setVelocityZ(0);
			}
		}
		
		if(packet instanceof ExplosionS2CPacket) {
			IExplosionS2CPacket explosionPacket = (IExplosionS2CPacket)packet;
			explosionPacket.setVelocityX(0);
			explosionPacket.setVelocityY(0);
			explosionPacket.setVelocityZ(0);
		}
	}
}