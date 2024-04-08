package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.retclient.module.modules.world.TileBreaker;

public class CmdTileBreaker extends Command {

	public CmdTileBreaker() {
		super("tilebreaker", "Breaks insta-break blocks within a certain radius", "[radius, toggle] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		TileBreaker module = (TileBreaker) Main.getInstance().moduleManager.tilebreaker;

		switch (parameters[0]) {
		case "radius":
			try {
				int radius = Integer.parseInt(parameters[1]);
				if (radius > 10) {
					radius = 10;
				} else if (radius < 1) {
					radius = 1;
				}
				module.setRadius(radius);
				CommandManager.sendChatMessage("TileBreaker radius set to " + radius);

			} catch (Exception e) {
				CommandManager.sendChatMessage("Invalid value. [1-10]");
			}
			break;
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setState(true);
				CommandManager.sendChatMessage("TileBreaker toggled ON");
			} else if (state.equals("off")) {
				module.setState(false);
				CommandManager.sendChatMessage("TileBreaker toggled OFF");
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
		case "radius":
			return new String[] { "1.0", "2.0", "5.0", "10.0" };
		default:
			return new String[] { "toggle", "radius" };
		}
	}
}
