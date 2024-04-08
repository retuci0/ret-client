package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import io.netty.buffer.Unpooled;
import net.retclient.Main;
import net.retclient.event.events.SendPacketEvent;
import net.retclient.event.listeners.SendPacketListener;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals extends Module implements SendPacketListener {

	public enum InteractType {
        INTERACT, ATTACK, INTERACT_AT
    }
	
	private BooleanSetting legit;
	
	public Criticals() {
		super(new KeybindSetting("key.criticals", "Criticals Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Criticals");
		this.setCategory(Category.Combat);
		this.setDescription("Makes all attacks into critical strikes.");
		
		legit = new BooleanSetting("criticals_legit", "Legit", "Whether or not we will use the 'legit' mode.", false);
		
		this.addSetting(legit);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(SendPacketListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(SendPacketListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	@Override
	public void OnSendPacket(SendPacketEvent event) {
		Packet<?> packet = event.GetPacket();
		if(packet instanceof PlayerInteractEntityC2SPacket) {
			PlayerInteractEntityC2SPacket playerInteractPacket = (PlayerInteractEntityC2SPacket) packet;
			PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
			playerInteractPacket.write(packetBuf);
			packetBuf.readVarInt();
			InteractType type = packetBuf.readEnumConstant(InteractType.class);
			
			if(type == InteractType.ATTACK) {
				MinecraftClient mc = MinecraftClient.getInstance();
				ClientPlayerEntity player = mc.player;
				if(player.isOnGround() && !player.isInLava() && !player.isSubmergedInWater()) {
					if(legit.getValue()) {
						player.jump();
					}else {
						ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
						networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.03125D, mc.player.getZ(), false));
						networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0625D, mc.player.getZ(), false));
						networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
					}
				}
			}
		}
	}
}
