package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.retclient.module.modules.misc.FastBreak;

public class CmdFastBreak extends Command {

	public CmdFastBreak() {
		super("fastbreak", "Decreases the time it takes to break blocks", "[multiplier, toggle] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		FastBreak module = (FastBreak) Main.getInstance().moduleManager.fastbreak;
		switch (parameters[0]) {
		case "multiplier":
			try {
				float multiplier = Float.parseFloat(parameters[1]);
				module.setMultiplier(multiplier);
				module.toggle();
				CommandManager.sendChatMessage("FastBreak multiplier set to " + multiplier + "x");
				module.toggle();
			} catch (Exception e) {
				CommandManager.sendChatMessage("Invalid value.");
			}
			break;
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setState(true);
				CommandManager.sendChatMessage("FastBreak toggled ON");
			} else if (state.equals("off")) {
				module.setState(false);
				CommandManager.sendChatMessage("FastBreak toggled OFF");
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
		case "multiplier":
			return new String[] { "0.5", "1.0", "1.15", "1.25", "1.5", "2.0" };
		default:
			return new String[] { "toggle", "multiplier" };
		}
	}
}