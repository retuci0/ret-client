package net.retclient.module.modules.render;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.event.listeners.TickListener;
import net.retclient.gui.Color;
import net.retclient.misc.ModuleUtils;
import net.retclient.misc.RainbowColor;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Box;

public class ChestESP extends Module implements RenderListener, TickListener {
	private Color currentColor;
	private RainbowColor rainbowColor;

	private ColorSetting color = new ColorSetting("chestesp_color", "Color", "Color", new Color(0, 1f, 1f));
	
	public BooleanSetting rainbow = new BooleanSetting("chestesp_rainbow", "Rainbow", "Rainbow", false);
	public FloatSetting effectSpeed = new FloatSetting("chestesp_effectspeed", "Effect Speed", "Effect Speed", 4f, 1f, 20f, 0.1f);
	
	
	public ChestESP() {
		super(new KeybindSetting("key.chestesp", "ChestESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("ChestESP");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see Chests with an ESP.");
		
		currentColor = color.getValue();
		rainbowColor = new RainbowColor();
		this.addSetting(color);
		this.addSetting(rainbow);
		this.addSetting(effectSpeed);
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
	public void OnRender(RenderEvent event) {
		ArrayList<BlockEntity> blockEntities = ModuleUtils.getTileEntities().collect(Collectors.toCollection(ArrayList::new));
		for(BlockEntity blockEntity : blockEntities) {
			if(blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity) {
				Box box = new Box(blockEntity.getPos());
				RenderUtils.draw3DBox(event.GetMatrixStack(), box, currentColor);
			}
		}
	}

	@Override
	public void OnUpdate(TickEvent event) {
		if(this.rainbow.getValue()) {
			this.rainbowColor.update(this.effectSpeed.getValue().floatValue());
			this.currentColor = this.rainbowColor.getColor();
		}else {
			this.currentColor = color.getValue();
		}
	}

}