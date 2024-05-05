package me.retucio.retclient.features.modules.render;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

// worst zoom you'll ever find

public class Zoom extends Module {
	
	private float fov;
	private double sens;
	private final Setting<Float> fovSetting = register(new Setting<>("FOV", 30f, 30f, 150f));
	private final Setting<Boolean> smoothCamera = register(new Setting<>("SmoothCam", true));
	private final Setting<Boolean> changeSens = register(new Setting<>("ChangeSens", false));
	private final Setting<Float> sensSetting = register(new Setting<>("Sensibility", 1.3f, 0.25f, 2f, v -> changeSens.getValue()));

	public Zoom() {
		super("Zoom", "Like OptiFine zoom", Category.RENDER, true, false, false);
	}
	
	@Override
	public void onEnable() {
		fov = mc.options.getFov().getValue();
		System.out.println("Fov: " + fov);
		System.out.println("Sens: " + sens);
		sens = mc.options.getMouseSensitivity().getValue();
		if (smoothCamera.getValue()) mc.options.smoothCameraEnabled = true;
	}
	
	@Override
	public void onDisable() {
		mc.options.getFov().setValue((int) fov);
		mc.options.getMouseSensitivity().setValue(sens);
		if (smoothCamera.getValue()) mc.options.smoothCameraEnabled = false;
	}
	
	@Override
	public void onUpdate() {
		mc.options.getFov().setValue(fovSetting.getValue().intValue());
		if (changeSens.getValue()) mc.options.getMouseSensitivity().setValue(sensSetting.getValue().doubleValue());
	}
}
