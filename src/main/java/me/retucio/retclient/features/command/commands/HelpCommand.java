package me.retucio.retclient.features.command.commands;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.features.command.Command;
import net.minecraft.util.Formatting;

public class HelpCommand extends Command {
	
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
    	
        HelpCommand.sendMessage("Commands: ");
        
        for (Command command : RetClient.commandManager.getCommands()) {
            StringBuilder builder = new StringBuilder(Formatting.GRAY.toString());
            
            builder.append(RetClient.commandManager.getPrefix());
            builder.append(command.getName());
            builder.append(" ");
            
            for (String cmd : command.getCommands()) {
                builder.append(cmd);
                builder.append(" ");
            }
            
            HelpCommand.sendMessage(builder.toString());
        }
    }
}