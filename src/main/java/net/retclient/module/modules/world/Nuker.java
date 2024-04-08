package net.retclient.module.modules.world;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Lists;

import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.event.listeners.TickListener;
import net.retclient.gui.Color;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.BlocksSetting;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class Nuker extends Module implements RenderListener, TickListener {
	private ColorSetting color = new ColorSetting("nuker_color", "Color", "Color", new Color(0, 1f, 1f));
	private FloatSetting radius = new FloatSetting("nuker_radius", "Radius", "Radius", 5f, 0f, 15f, 1f);
	private BlocksSetting blacklist = new BlocksSetting("nuker_blacklist", "Blacklist", "Blocks that will not be broken by Nuker.", Lists.newArrayList());
	
	public Nuker() {
		super(new KeybindSetting("key.nuker", "Nuker Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Nuker");
		this.setCategory(Category.World);
		this.setDescription("Destroys blocks around the player.");
		
		this.addSetting(radius);
		this.addSetting(color);
		this.addSetting(blacklist);
	}

	public void setRadius(int radius) {
		this.radius.setValue((float)radius);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(RenderListener.class, this);
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {
	}

	@Override
	public void OnUpdate(TickEvent event) {
		int rad = radius.getValue().intValue();
		for (int x = -rad; x < rad; x++) {
			for (int y = rad; y > -rad; y--) {
				for (int z = -rad; z < rad; z++) {
					BlockPos blockpos = new BlockPos(MC.player.getBlockX() + x, (int) MC.player.getBlockY() + y,
							(int) MC.player.getBlockZ() + z);
					Block block = MC.world.getBlockState(blockpos).getBlock();
					if (block == Blocks.AIR || blacklist.getValue().contains(block))
						continue;
					
					MC.player.networkHandler.sendPacket(
							new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockpos, Direction.NORTH));
					MC.player.networkHandler
							.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockpos, Direction.NORTH));
				}
			}
		}
	}

	@Override
	public void OnRender(RenderEvent event) {
		int rad = radius.getValue().intValue();
		for (int x = -rad; x < rad; x++) {
			for (int y = rad; y > -rad; y--) {
				for (int z = -rad; z < rad; z++) {
					BlockPos blockpos = new BlockPos(MC.player.getBlockX()+ x, MC.player.getBlockY() + y,
							MC.player.getBlockZ()+ z);
					Block block = MC.world.getBlockState(blockpos).getBlock();

					if (block == Blocks.AIR || block == Blocks.WATER || block == Blocks.LAVA  || blacklist.getValue().contains(block))
						continue;

					RenderUtils.draw3DBox(event.GetMatrixStack(), new Box(blockpos), color.getValue());
				}
			}
		}
	}
}