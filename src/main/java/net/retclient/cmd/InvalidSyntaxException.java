package net.retclient.cmd;

import net.minecraft.util.Formatting;

public class InvalidSyntaxException extends CommandException {
	private static final long serialVersionUID = 1L;
	
	public InvalidSyntaxException(Command cmd) {
		super(cmd);
	}

	@Override
	public void PrintToChat() {
		CommandManager.sendChatMessage("Invalid Usage! Usage: " + Formatting.LIGHT_PURPLE + ".ret " + cmd.getName() + " " + cmd.getSyntax() + Formatting.RESET);
	}
}