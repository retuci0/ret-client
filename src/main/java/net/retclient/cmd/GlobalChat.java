/*
* Aoba Hacked Client
* Copyright (C) 2019-2024 coltonk9043
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.retclient.cmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import net.retclient.Main;
import net.retclient.settings.SettingManager;
import net.retclient.settings.types.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

public class GlobalChat {
	
	public enum ChatType{
		Minecraft,
		Global
	}
	
	public static ChatType chatType = ChatType.Minecraft;

	public final List<ChatHudLine.Visible> messages = Lists.newArrayList();

	private Gson gson;
	private Socket socket;
	private Thread chatSocketListenerThread;
	private PrintWriter out;
	private BufferedReader in;
	private boolean started = false;
	
	private BooleanSetting enabled;
	
	public GlobalChat() {
		gson = new Gson();
		enabled = new BooleanSetting("global_chat_enabled", "Whether or not global chat is enabled or disabled.", true);
		SettingManager.registerSetting(this.enabled, Main.getInstance().settingManager.modules_category);
	}
	
	private void Send(String json) {
		if(out != null) {
			out.print(json);
			out.flush();
		}
	}

	public void SendMessage(String message) {
		Send(gson.toJson(new MessageAction(message, null)));
	}
	
	private void SendChatMessage(String message) {
		MinecraftClient MC = MinecraftClient.getInstance();
		if(MC != null && MC.inGameHud != null) {
			this.messages.add(0, new ChatHudLine.Visible(MC.inGameHud.getTicks(), Text.of(message).asOrderedText(), MessageIndicator.system(), false));
			
		}
	}
	
	
	public void StartListener() {
		if(started) {
			System.out.println("Socket listener already started.");
			return;
		}
		
		try {
			started = true;

			socket = new Socket("18.221.222.43", 80);
			out = new PrintWriter(socket.getOutputStream(), false);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			chatSocketListenerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// Send the connection action to the server.
						Send(gson.toJson(new ConnectAction(MinecraftClient.getInstance().getSession().getUsername())));
						
						String json = in.readLine();
						while(json != null && MinecraftClient.getInstance() != null) {
							MessageResponse response = new Gson().fromJson(json, MessageResponse.class);
							if(response != null) {
								String user = response.getUser();
								String chatMessage = response.getMessage();
								if(user != null && chatMessage != null) {
									SendChatMessage(String.format("<%s> %s", user, chatMessage));
								}
							}
							json = in.readLine();
						}
						
						out.close();
						in.close();
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});  
			chatSocketListenerThread.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void RemoveListener() {
		Send(gson.toJson(new GenericAction("disconnect")));
	}
}

/**
 * Defines classes that will be parsed to Json and passed to the chat server.
 */
class GenericAction{
	public String action;
	public GenericAction(String action) { this.action = action; }
}

class ConnectAction extends GenericAction {
	public String username;
	public ConnectAction(String username) { 
		super("connect"); 
		this.username = username;
	}
}

class MessageAction extends GenericAction{
	public String message;
	public String to;
	public MessageAction(String message, String to) { 
		super("message"); 
		this.message = message;
		this.to = to;
	}
}

/**
 *  Server Response
 */
class MessageResponse {
	private String message;
	private String user;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
}



