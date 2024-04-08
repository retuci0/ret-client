package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.retclient.module.modules.combat.Reach;

public class CmdReach extends Command {

	public CmdReach() {
		super("reach", "Allows the player to reach further.", "[toggle/distance] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		Reach module = (Reach) Main.getInstance().moduleManager.reach;

		switch (parameters[0]) {
		case "distance":
			try {
				float distance = Float.parseFloat(parameters[1]);
				module.setReachLength(distance);
				CommandManager.sendChatMessage("Reach distance set to " + distance);

			} catch (Exception e) {
				CommandManager.sendChatMessage("Invalid value.");
			}
			break;
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setState(true);
				CommandManager.sendChatMessage("Reach toggled ON");
			} else if (state.equals("off")) {
				module.setState(false);
				CommandManager.sendChatMessage("Reach toggled OFF");
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
		case "distance:":
			return new String[] { "1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0" };
		default:
			return new String[] { "toggle" };
		}
	}
}