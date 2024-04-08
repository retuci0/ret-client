package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CrystalAura extends Module implements TickListener {
	
	private FloatSetting radius;
	
	public CrystalAura() {
		super(new KeybindSetting("key.crystalaura", "Crystal Aura Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("CrystalAura");
		this.setCategory(Category.Combat);
		this.setDescription("Attacks anything within your personal space.");
		
		radius = new FloatSetting("crystalaura_radius", "Radius", "Radius, in blocks, that you can place/attack a crystal.", 5f, 1f, 15f, 1f);
		this.addSetting(radius);
	}

	@Override
	public void onDisable() {
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
		for (PlayerEntity player : MC.world.getPlayers()) {
			if (player == MC.player || MC.player.distanceTo(player) > this.radius.getValue()) {
				continue;
			}
			// Get the block position below the player.
			BlockPos entityPos = player.getBlockPos();
			BlockPos check = entityPos.add(0, -1, 0);
			BlockState bs = MC.world.getBlockState(check);
			Block block = bs.getBlock();
			if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK)
				continue;

			for (int slot = 0; slot < 9; slot++) {
				Item item = MC.player.getInventory().getStack(slot).getItem();
				if (item == Items.END_CRYSTAL) {
					MC.player.getInventory().selectedSlot = slot;
					break;
				}
			}
			BlockHitResult rayTrace = new BlockHitResult(check.toCenterPos(), Direction.UP, check, false);
			MC.interactionManager.interactBlock(MC.player, Hand.MAIN_HAND, rayTrace);
			this.MC.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
		}

		// Hit the entity that has been found.
		Entity entity = null;
		for (Entity ent : MC.world.getEntities()) {
			if (MC.player.distanceTo(ent) < radius.getValue()) {
				if (ent instanceof EndCrystalEntity) {
					entity = ent;
					break;
				}
			}
		}
	
		if(entity != null) {
			MC.player.swingHand(Hand.MAIN_HAND);
			MC.interactionManager.attackEntity(MC.player, entity);
			MC.player.attack(entity);
		}
	}
}