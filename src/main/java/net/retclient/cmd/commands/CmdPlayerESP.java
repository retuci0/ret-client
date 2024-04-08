package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.retclient.module.modules.render.PlayerESP;

public class CmdPlayerESP extends Command {

	public CmdPlayerESP() {
		super("playeresp", "Allows the player to see other players through walls.", "[toggle] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		PlayerESP module = (PlayerESP) Main.getInstance().moduleManager.playeresp;

		switch (parameters[0]) {
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setState(true);
				CommandManager.sendChatMessage("PlayerESP toggled ON");
			} else if (state.equals("off")) {
				module.setState(false);
				CommandManager.sendChatMessage("PlayerESP toggled OFF");
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
		default:
			return new String[] { "toggle" };
		}
	}
}
