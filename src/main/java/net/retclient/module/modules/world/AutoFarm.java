package net.retclient.module.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.misc.ModuleUtils;
import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;

public class AutoFarm extends Module implements TickListener {
	private FloatSetting radius;

	public AutoFarm() {
		super(new KeybindSetting("key.autofarm", "AutoFarm Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AutoFarm");
		this.setCategory(Category.World);
		this.setDescription("Destroys blocks that can be instantly broken around the player.");
		this.radius = new FloatSetting("autofarm_radius", "Radius", "Radius", 5f, 0f, 15f, 1f);
		this.addSetting(radius);
	}

	public void setRadius(int radius) {
		this.radius.setValue((float)radius);
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
		int rad = radius.getValue().intValue();
		for (int x = -rad; x < rad; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -rad; z < rad; z++) {
					BlockPos blockpos = new BlockPos(MC.player.getBlockPos().getX() + x, MC.player.getBlockPos().getY() + y,
							MC.player.getBlockPos().getZ() + z);
					Block block = MC.world.getBlockState(blockpos).getBlock();
					BlockState blockstate = MC.world.getBlockState(blockpos);
					if (block instanceof CropBlock) {
						CropBlock crop = (CropBlock) block;
						if (!crop.canGrow(MC.world, null, blockpos, blockstate)) {
							MC.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK,blockpos, Direction.NORTH));
							MC.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockpos, Direction.NORTH));
						}else {
							boolean b = false;
							for(int i = 0; i< 9; i++) {
								ItemStack stack = MC.player.getInventory().getStack(i);
								if(stack.getItem() == Items.BONE_MEAL) {
							    	MC.player.getInventory().selectedSlot = i;
							    	b = true;
							    	break;
							    }
							}
							if(b) {
								BlockHitResult rayTrace = new BlockHitResult(new Vec3d(0,0,0), Direction.UP, blockpos, false);
								
								this.MC.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, rayTrace, 0));
							}
						}
					}else if (block instanceof FarmlandBlock) {
						BlockPos blockAbovePos = new BlockPos((int) MC.player.getBlockPos().getX() + x, (int) MC.player.getBlockPos().getY() + y + 1,
								(int) MC.player.getBlockPos().getZ() + z);
						Block blockAbove = MC.world.getBlockState(blockAbovePos).getBlock();
						if(blockAbove == Blocks.AIR) {
							boolean b = false;
							for(int i = 0; i< 9; i++) {
								ItemStack stack = MC.player.getInventory().getStack(i);
								if(ModuleUtils.isPlantable(stack)) {
							    	MC.player.getInventory().selectedSlot = i;
							    	b = true;
							    	break;
							    }
							}
							if(b) {
								BlockHitResult rayTrace = new BlockHitResult(new Vec3d(0,0,0), Direction.UP, blockpos, false);
								this.MC.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, rayTrace, 0));
							}
						}
					}
				}
			}
		}
	}
}