package net.retclient.module.modules.render;

import org.lwjgl.glfw.GLFW;

import net.retclient.interfaces.ISimpleOption;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Fullbright extends Module {

	private double previousValue = 0.0;
	public Fullbright() {
		super(new KeybindSetting("key.fullbright", "Fullbright Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Fullbright");
		this.setCategory(Category.Render);
		this.setDescription("Maxes out the brightness.");
	}

	@Override
	public void onDisable() {
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma =
				(ISimpleOption<Double>)(Object)MC.options.getGamma();
		gamma.forceSetValue(previousValue);
	}

	@Override
	public void onEnable() {
		this.previousValue = MC.options.getGamma().getValue();
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma =
				(ISimpleOption<Double>)(Object)MC.options.getGamma();
		gamma.forceSetValue(10000.0);
	}

	@Override
	public void onToggle() {

	}
}