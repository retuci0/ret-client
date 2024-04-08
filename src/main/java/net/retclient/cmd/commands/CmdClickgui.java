package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.InvalidSyntaxException;
import net.minecraft.client.util.InputUtil;

public class CmdClickgui extends Command {

	public CmdClickgui() {
		super("clickgui", "Allows the player to see chest locations through ESP", "[set/open] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		switch (parameters[0]) {
		case "set":
			if (parameters.length != 2)
				throw new InvalidSyntaxException(this);
			char keybind = Character.toUpperCase(parameters[1].charAt(0));
			Main.getInstance().hudManager.clickGuiButton.setValue(InputUtil.fromKeyCode(keybind, 0));
			break;
		case "open":
			Main.getInstance().hudManager.setClickGuiOpen(true);
			break;
		default:
			throw new InvalidSyntaxException(this);
		}
	}

	@Override
	public String[] getAutocorrect(String previousParameter) {
		switch (previousParameter) {
		default:
			return new String[] { "set", "open" };
		}
	}
}
