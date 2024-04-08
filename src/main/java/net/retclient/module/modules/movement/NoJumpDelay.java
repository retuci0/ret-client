package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.mixin.interfaces.ILivingEntity;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class NoJumpDelay extends Module implements TickListener {

	private FloatSetting delay;
	
	public NoJumpDelay() {
		super(new KeybindSetting("key.nojumpdelay", "NoJumpDelay Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("NoJumpDelay");
		this.setCategory(Category.Movement);
		this.setDescription("Makes it so the user can jump very quickly.");
		
		delay = new FloatSetting("nojumpdelay_delay", "Delay", "NoJumpDelay Delay", 1f, 0.0f, 20.0f, 1f);
		this.addSetting(delay);
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
		ILivingEntity ent = (ILivingEntity)MC.player;
		if(ent.getJumpCooldown() > delay.getValue()) {
			ent.setJumpCooldown(delay.getValue().intValue());
		}
	}
}