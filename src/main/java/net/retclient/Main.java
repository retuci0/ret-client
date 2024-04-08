package net.retclient;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	public static Client instance;
	
	@Override
	public void onInitialize()
	{
		instance = new Client();
		instance.Initialize();
	}
	
	/**
	 * Returns the singleton instance of Ret client.
	 * @return Ret Client
	 */
	public static Client getInstance() {
		return instance;
	}

}