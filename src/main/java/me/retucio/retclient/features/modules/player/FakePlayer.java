package me.retucio.retclient.features.modules.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import me.retucio.retclient.features.command.Command;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FakePlayer extends Module {

	private final Setting<String> playerName = register(new Setting<>("PlayerName", "popbob"));
	private OtherClientPlayerEntity FakePlayer;
	
	public FakePlayer() {
		super("FakePlayer", "Summons a copy of the current player", Category.PLAYER, true, false, false);
	}
	
    @Override
    public void onEnable() {
        FakePlayer = null;

        if (nullCheck()) {
            disable();
            return;
        }

        try {
            FakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString(getUuid(playerName.getValue())), playerName.getValue()));
        } 
        
        catch (Exception e) {
            FakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString(getUuid(mc.player.getName().getString())), playerName.getValue()));
            Command.sendMessage("Failed to load uuid, setting another one.");
        }
        
        Command.sendMessage(String.format("%s has been spawned.", playerName.getValue()));

        FakePlayer.copyFrom(mc.player);
        FakePlayer.headYaw = mc.player.getHeadYaw();
        mc.world.addEntity(FakePlayer);
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;
        mc.world.removeEntity(FakePlayer.getId(), Entity.RemovalReason.UNLOADED_WITH_PLAYER);
    }

    public static String getUuid(String name) {
        Gson gson = new Gson();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        
        try {
            String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) return "invalid name";
            
            JsonObject UUIDObject = gson.fromJson(UUIDJson, JsonObject.class);
            return reformatUuid(UUIDObject.get("id").toString());
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return "error";
    }

    private static String reformatUuid(String uuid) {
        String longUuid = "";

        longUuid += uuid.substring(1, 9) + "-";
        longUuid += uuid.substring(9, 13) + "-";
        longUuid += uuid.substring(13, 17) + "-";
        longUuid += uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);

        return longUuid;
    }
}