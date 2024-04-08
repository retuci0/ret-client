package net.retclient.mixin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.retclient.Main;
import net.retclient.cmd.Command;
import net.retclient.cmd.CommandManager;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.OrderedText;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {
	@Shadow
	private TextFieldWidget textField;
	@Shadow
	@Nullable
	private ParseResults<CommandSource> parse;
	@Shadow
	private CompletableFuture<Suggestions> pendingSuggestions;
	@Shadow
	private List<OrderedText> messages;
	
	@Shadow
	public abstract void show(boolean narrateFirstSuggestion);
	
	@Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;getCursor()I", ordinal = 0) }, method = "refresh()V", cancellable = true)
	private void onRefresh(CallbackInfo ci) {
		String prefix = CommandManager.PREFIX.getValue();
		String string = this.textField.getText();
		
		if(string.length() > 0) {
			int cursorPos = this.textField.getCursor();
			String string2 = string.substring(0, cursorPos);
			
			if(string2.charAt(0) == CommandManager.PREFIX.getValue().charAt(0)) {
				int j = 0;
				Matcher matcher = Pattern.compile("(\\s+)").matcher(string2);
				while (matcher.find()) {
					j = matcher.end();
				}
				
				SuggestionsBuilder builder = new SuggestionsBuilder(string2, j);
				if(string2.length() <= prefix.length()) {
					if(prefix.startsWith(string2)) {
						builder.suggest(prefix + " ");
					}else {
						return;
					}
				}else {
					int count = StringUtils.countMatches(string2, " ");
					List<String> seperated = Arrays.asList(string2.split(" "));
					if(count == 1) {
						for (Object strObj : Main.getInstance().commandManager.getCommands().keySet().toArray()) {
							String str = (String) strObj;
							builder.suggest(str + " ");
						}
					}else {
						if(seperated.size() <= 1) return;
						Command c = Main.getInstance().commandManager.getCommandBySyntax(seperated.get(1));
						if (c == null) {
							messages.add(Text.of("Aoba: No commands found with name: " + string2).asOrderedText());
							return;
						}
						
						String[] suggestions = c.getAutocorrect(seperated.get(seperated.size() - 1));

						if(suggestions == null || suggestions.length == 0) return;
						for(String str : suggestions) {
							builder.suggest(str + " ");
						}
					}
				}
				
				this.pendingSuggestions = builder.buildFuture();
				this.show(false);
			}
		}
	}
}
