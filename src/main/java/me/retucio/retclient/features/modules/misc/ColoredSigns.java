package me.retucio.retclient.features.modules.misc;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.features.modules.Module;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

public final class ColoredSigns extends Module {

    public ColoredSigns() {
        super("ColoredSigns", "Allows the use of colours in signs using the & symbol (patched in Spigot servers)", Category.MISC, true, false, false);
    }

    @Subscribe
    public void packetSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof UpdateSignC2SPacket) {
            final UpdateSignC2SPacket packet = (UpdateSignC2SPacket) event.getPacket();
            
            for (int i = 0; i < 4; i++) {
                packet.getText()[i] = packet.getText()[i].replace("&", "\247" + "\247a");
            }
        }
    }
}