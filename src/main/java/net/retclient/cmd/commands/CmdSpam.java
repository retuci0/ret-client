package net.retclient.cmd.commands;

import net.retclient.cmd.Command;
import net.retclient.cmd.InvalidSyntaxException;

public class CmdSpam extends Command {

	public CmdSpam() {
		super("spam", "Spams the chat with a certain message.", "[times] [message]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length < 2)
			throw new InvalidSyntaxException(this);

		// Combines the "parameters" into a string to be printed.
		String message = "";
		for (int msg = 1; msg < parameters.length; msg++) {
			message = message + parameters[msg] + " ";
		}
		
		// Prints out that message X number of times.
		for (int i = 0; i < Integer.parseInt(parameters[0]); i++) {
			mc.player.networkHandler.sendChatMessage(message);
		}

	}

	@Override
	public String[] getAutocorrect(String previousParameter) {
		switch (previousParameter) {
		default:
			return new String[] { "Aoba is an amazing client!" };
		}
	}
}