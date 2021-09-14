package someoneelse.betternetherreforged.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

import someoneelse.betternetherreforged.BetterNether;

public class MixinConnector implements IMixinConnector {
	@Override
	public void connect() {
		BetterNether.LOGGER.debug("Better Nether: Connecting Mixins...");
		Mixins.addConfiguration("betternether.mixins.json");
		//BetterNether.isUsingMixin = true;
		BetterNether.LOGGER.info("Better Nether: Mixin Connected!");
	}
}
