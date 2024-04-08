package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.retclient.module.modules.misc.Timer;

public class CmdTimer extends Command {

	public CmdTimer() {
		super("timer", "Speeds up the game.", "[toggle/multiplier] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		Timer module = (Timer) Main.getInstance().moduleManager.timer;

		switch (parameters[0]) {
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setState(true);
				CommandManager.sendChatMessage("Timer toggled ON");
			} else if (state.equals("off")) {
				module.setState(false);
				CommandManager.sendChatMessage("Timer toggled OFF");
			} else {
				CommandManager.sendChatMessage("Invalid value. [ON/OFF]");
			}
			break;
		case "multiplier":
			try {
				float param1 = Float.parseFloat(parameters[1]);
				Timer timer = (Timer) Main.getInstance().moduleManager.timer;
				timer.setMultipler(param1);
				CommandManager.sendChatMessage("Timer multiplier set to " + param1);

			} catch (Exception e) {
				CommandManager.sendChatMessage("Invalid value.");
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