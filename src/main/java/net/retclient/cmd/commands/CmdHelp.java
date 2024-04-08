package net.retclient.cmd.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;
import net.retclient.module.Module;
import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;

public class CmdHelp extends Command {

	int indexesPerPage = 5;

	public CmdHelp() {
		super("help", "Shows the avaiable commands.", "[page, module]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length <= 0) {
			ShowCommands(1);
		} else if (StringUtils.isNumeric(parameters[0])) {
			int page = Integer.parseInt(parameters[0]);
			ShowCommands(page);
		} else {
			Module module = Main.getInstance().moduleManager.getModuleByName(parameters[0]);
			if (module == null) {
				CommandManager.sendChatMessage("Could not find Module '" + parameters[0] + "'.");
			} else {
				String title = "------------ " + Formatting.LIGHT_PURPLE + module.getName() + " Help" + Formatting.RESET + " ------------";
				String unformatted_title = "------------ " + module.getName() + " Help ------------";
				CommandManager.sendChatMessage(title);
				CommandManager.sendChatMessage("Name: " + Formatting.LIGHT_PURPLE + module.getName() + Formatting.RESET);
				CommandManager.sendChatMessage("Description: " + Formatting.LIGHT_PURPLE + module.getDescription() + Formatting.RESET);
				CommandManager.sendChatMessage("Keybind: " + Formatting.LIGHT_PURPLE + module.getBind().getValue().getTranslationKey() + Formatting.RESET);
				CommandManager.sendChatMessage("-".repeat(unformatted_title.length() - 2)); // mc font characters are not the same width but eh..
			}
		}

	}

	private void ShowCommands(int page) {
		String title = "------------ Help [Page " + page + " of 5] ------------";  // TODO: remove hardcoded page length
		CommandManager.sendChatMessage(title);
		CommandManager.sendChatMessage("Use " + Formatting.LIGHT_PURPLE + ".ret help [n]" + Formatting.RESET + " to get page n of help.");

		// Fetch the commands and dislays their syntax on the screen.
		HashMap<String, Command> commands = Main.getInstance().commandManager.getCommands();
		Set<String> keySet = commands.keySet();
		ArrayList<String> listOfCommands = new ArrayList<String>(keySet);
		 
		for (int i = (page - 1) * indexesPerPage; i <= (page * indexesPerPage); i++) {
			if (i >= 0 && i < Main.getInstance().commandManager.getNumOfCommands()) {
				CommandManager.sendChatMessage(" .ret " + listOfCommands.get(i));
			}
		}
		CommandManager.sendChatMessage("-".repeat(title.length() - 2)); // mc font characters are not the same width but eh..
	}

	@Override
	public String[] getAutocorrect(String previousParameter) {
		CommandManager cm = Main.getInstance().commandManager;
		int numCmds = cm.getNumOfCommands();
		String[] commands = new String[numCmds];

		Set<String> cmds = Main.getInstance().commandManager.getCommands().keySet();
		int i = 0;
        for (String x : cmds)
        	commands[i++] = x;
		
		return commands;
	}

}