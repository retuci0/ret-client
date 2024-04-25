package me.retucio.retclient.features.modules.combat;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class AutoTotem extends Module {
	
	private int delay;
	private boolean holdingTotem;
	

	private final Setting<Boolean> overrideOffhandItem = register(new Setting<>("Override", false));
	private final Setting<Float> swapTotemDelay = register(new Setting<>("Swap delay", 1f, 0f, 10f, v -> true));
	private final Setting<Float> popTotemDelay = register(new Setting<>("Pop delay", 1f, 0f, 10f, v -> true));
	
	public AutoTotem() {
		super("AutoTotem", "Automatically puts totems in your offhand", Category.COMBAT, true, false, false);
	}
	
	@Override
	public void onUpdate() {
		if (holdingTotem && mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
			delay = Math.max(popTotemDelay.getValue().intValue(), delay);
		}

		holdingTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;

		if (delay > 0) {
			delay--;
			return;
		}

		if (holdingTotem || (!mc.player.getOffHandStack().isEmpty() && !overrideOffhandItem.getValue().booleanValue())) {
			return;
		}

		if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
			for (int i = 9; i < 45; i++) {
				
				if (mc.player.getInventory().getStack(i >= 36 ? i - 36 : i).getItem() == Items.TOTEM_OF_UNDYING) {
					boolean itemInOffhand = !mc.player.getOffHandStack().isEmpty();
					
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);

					if (itemInOffhand)
						mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);

					delay = swapTotemDelay.getValue().intValue();
					return;
				}
			}
			
		} else {
			
			for (int i = 0; i < 9; i++) {
				
				if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
					
					if (i != mc.player.getInventory().selectedSlot) {
						mc.player.getInventory().selectedSlot = i;
						mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
					}

					mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
					delay = swapTotemDelay.getValue().intValue();
					
					return;
				}
			}
		}
	}
}