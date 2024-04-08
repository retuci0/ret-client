package net.retclient.cmd.commands;

import net.retclient.cmd.Command;
import net.retclient.cmd.InvalidSyntaxException;

public class CmdTP extends Command {

	public CmdTP() {
		super("tp", "Teleports the player certain blocks away (Vanilla only)", "[x] [y] [z]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 3)
			throw new InvalidSyntaxException(this);
	
			mc.player.setPosition(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2]));
	}

	@Override
	public String[] getAutocorrect(String previousParameter) {
		return new String[] {"0 0 0"};
	}
}