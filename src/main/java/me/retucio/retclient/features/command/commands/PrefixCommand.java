package me.retucio.retclient.features.command.commands;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.features.command.Command;
import net.minecraft.util.Formatting;

public class PrefixCommand extends Command {
	
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(Formatting.GREEN + "Current prefix is " + RetClient.commandManager.getPrefix());
            return;
        }
        
        RetClient.commandManager.setPrefix(commands[0]);
        Command.sendMessage("Prefix changed to " + Formatting.GRAY + commands[0]);
    }
}