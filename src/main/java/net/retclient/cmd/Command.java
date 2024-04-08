package net.retclient.cmd;

import java.util.Objects;

import net.retclient.Client;
import net.minecraft.client.MinecraftClient;

public abstract class Command {
	protected final String name;
	protected final String description;
	protected final String syntax;
	
	protected static final MinecraftClient mc = Client.MC;
	
	public Command(String name, String description, String syntax) {
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
		this.syntax = Objects.requireNonNull(syntax);
	}
	
	/**
	 * Gets the name of the command.
	 * @return The name of the command.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the description of the command.
	 * @return The description of the command.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Gets the syntax of the command.
	 * @return The syntax of the command.
	 */
	public String getSyntax() {
		return this.syntax;
	}
	
	/**
	 * Runs the intended action of the command.
	 * @param parameters The parameters being passed.
	 */
	public abstract void runCommand(String[] parameters) throws InvalidSyntaxException;
	
	/**
	 * Gets the next Autocorrect suggestions given the last typed parameter.
	 * @param previousParameter
	 */
	public abstract String[] getAutocorrect(String previousParameter);
}
