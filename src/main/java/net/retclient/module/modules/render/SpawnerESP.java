package net.retclient.module.modules.render;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.gui.Color;
import net.retclient.misc.ModuleUtils;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Box;

public class SpawnerESP extends Module implements RenderListener {

	private ColorSetting color = new ColorSetting("spawneresp_color", "Color", "Color", new Color(0, 1f, 1f));
	
	public SpawnerESP() {
		super(new KeybindSetting("key.spawneresp", "SpawnerESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("SpawnerESP");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see spawners with an ESP.");
		
		this.addSetting(color);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(RenderListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnRender(RenderEvent event) {
		ArrayList<BlockEntity> blockEntities = ModuleUtils.getTileEntities().collect(Collectors.toCollection(ArrayList::new));
		
		for(BlockEntity blockEntity : blockEntities) {
			if(blockEntity instanceof MobSpawnerBlockEntity) {
				Box box = new Box(blockEntity.getPos());
				RenderUtils.draw3DBox(event.GetMatrixStack(), box, color.getValue());
			}
		}
	}
}