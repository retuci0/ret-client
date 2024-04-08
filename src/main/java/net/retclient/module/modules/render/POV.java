package net.retclient.module.modules.render;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.misc.FakePlayerEntity;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class POV extends Module implements TickListener {
	private FakePlayerEntity fakePlayer;
	private String povString = null;
	private Entity povEntity = null;
	
	private boolean fakePlayerSpawned = false;
	
	
	public POV() {
		super(new KeybindSetting("key.pov", "POV Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("POV");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see someone else's point-of-view.");
	}

	@Override
	public void onDisable() {
		MinecraftClient.getInstance().setCameraEntity(MC.player);
		if(fakePlayer != null) {
			fakePlayer.despawn();
			MC.world.removeEntity(-3, null);
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

	public void setEntityPOV(String entity) {
		this.povString = entity;
	}

	public Entity getEntity() {
		return this.povEntity;
	}
	
	public PlayerEntity getEntityAsPlayer() {
		if(this.povEntity instanceof PlayerEntity) {
			return (PlayerEntity) this.povEntity;
		}else {
			return null;
		}
	}

	@Override
	public void OnUpdate(TickEvent event) {
		ClientPlayerEntity player = MC.player;
		povEntity = null;
		for(Entity entity : MC.world.getPlayers()) {
			if(entity.getName().getString().equals(povString)) {
				povEntity = entity;
			}
		}
		if(MinecraftClient.getInstance().getCameraEntity() == povEntity) {
			if(!fakePlayerSpawned) {
				fakePlayer = new FakePlayerEntity();
				fakePlayer.copyFrom(player);
				fakePlayer.headYaw = player.headYaw;
				MC.world.addEntity(fakePlayer);
			}
			fakePlayer.copyFrom(player);
			fakePlayer.headYaw = player.headYaw;
		}else {
			if(fakePlayer != null) {
				fakePlayer.despawn();
				MC.world.removeEntity(-3, null);
			}
			
			if(povEntity == null) {
				MinecraftClient.getInstance().setCameraEntity(MC.player);
			}else {
				MinecraftClient.getInstance().setCameraEntity(povEntity);
			}
		}
	}
}