package me.retucio.retclient.features.command.commands;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.features.command.Command;
import me.retucio.retclient.features.modules.Module;

public class ToggleCommand extends Command {
	
    public ToggleCommand() {
        super("toggle", new String[] {"<module>"});
    }

    @Override public void execute(String[] var1) {
        if (var1.length < 1 || var1[0] == null) {
            notFound();
            return;
        }
        
        Module mod = RetClient.moduleManager.getModuleByName(var1[0]);
        
        if (mod == null) {
            notFound();
            return;
        }
        
        mod.toggle();
    }

    private void notFound() {
        sendMessage("Module is not found.");
    }
}
