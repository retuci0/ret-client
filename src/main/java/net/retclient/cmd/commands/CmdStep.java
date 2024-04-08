package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.retclient.module.modules.movement.Step;

public class CmdStep extends Command {

	public CmdStep() {
		super("step", "Allows the player to step up blocks", "[toggle/height] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2) {
			throw new InvalidSyntaxException(this);
		}
		Step module = (Step) Main.getInstance().moduleManager.step;

		switch (parameters[0]) {
		case "height":
			try {
				float height = Float.parseFloat(parameters[1]);
				module.setStepHeight(height);
				CommandManager.sendChatMessage("Step height set to " + height);

			} catch (Exception e) {
				CommandManager.sendChatMessage("Invalid value.");
			}
			break;
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setState(true);
				CommandManager.sendChatMessage("Step toggled ON");
			} else if (state.equals("off")) {
				module.setState(false);
				CommandManager.sendChatMessage("Step toggled OFF");
			} else {
				CommandManager.sendChatMessage("Invalid value. [ON/OFF]");
			}
			break;
		default:
			throw new InvalidSyntaxException(this);
		}
	}

	@Override
	public String[] getAutocorrect(String previousParameter) {
		switch (previousParameter) {
		case "toggle":
			return new String[] { "on", "off" };
		case "height":
			return new String[] { "0.5", "1.0", "1.5", "2.0", "5.0", "10.0" };
		default:
			return new String[] { "toggle", "height" };
		}
	}
}
