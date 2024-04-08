package net.retclient.module.modules.render;

import java.util.List;
import org.lwjgl.glfw.GLFW;
import com.google.common.collect.Lists;
import net.retclient.interfaces.ISimpleOption;
import net.retclient.module.Module;
import net.retclient.settings.types.BlocksSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;

public class XRay extends Module {
	public BlocksSetting blocks;

	public XRay() {
		super(new KeybindSetting("key.xray", "XRay Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("XRay");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see ores.");

		blocks = new BlocksSetting("xray_blocks", "Blocks", "Blocks that can be seen in Xray",
				Lists.newArrayList(Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK, Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK,
						Blocks.GOLD_ORE, Blocks.GOLD_BLOCK, Blocks.IRON_ORE, Blocks.IRON_BLOCK, Blocks.COAL_ORE,
						Blocks.COAL_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.REDSTONE_ORE, Blocks.LAPIS_ORE,
						Blocks.LAPIS_BLOCK, Blocks.NETHER_QUARTZ_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.STONE_BRICKS,
						Blocks.OAK_PLANKS, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
						Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_COAL_ORE));
		blocks.setOnUpdate((List<Block> block) -> ReloadRenderer(block));
		this.addSetting(blocks);
	}

	@Override
	public void onDisable() {
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma = (ISimpleOption<Double>) (Object) MC.options.getGamma();
		gamma.forceSetValue(1.0);
		MC.worldRenderer.reload();
	}

	@Override
	public void onEnable() {
		MC.worldRenderer.reload();
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma = (ISimpleOption<Double>) (Object) MC.options.getGamma();
		gamma.forceSetValue(10000.0);

	}

	@Override
	public void onToggle() {

	}

	public boolean isXRayBlock(Block b) {
		List<Block> blockList = blocks.getValue();
		if (blockList.contains(b)) {
			return true;
		}
		return false;
	}
	
	public void ReloadRenderer(List<Block> block) {
		if(MC.worldRenderer != null && this.getState()) {
			MC.worldRenderer.reload();
		}	
	}
}