package net.retclient.cmd.commands;

import net.retclient.Main;
import net.retclient.Client;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.InvalidSyntaxException;
import net.minecraft.client.font.TextRenderer;

public class CmdFont extends Command {

	public CmdFont() {
		super("font", "Sets the HUD font.", "[set] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		Client ret = Main.getInstance();
		
		switch (parameters[0]) {
		case "set":
			try {
				String font = parameters[1];
				TextRenderer t = ret.fontManager.fontRenderers.get(font);
				if(t != null) {
					ret.fontManager.SetRenderer(t);
				}
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
		case "set":
			Client ret = Main.getInstance();
			
			String[] suggestions = new String[ret.fontManager.fontRenderers.size()];

			int i = 0;
			for (String fontName : ret.fontManager.fontRenderers.keySet())
				suggestions[i++] = fontName;

			return suggestions;
		default:
			return new String[] { "set" };
		}
	}
}