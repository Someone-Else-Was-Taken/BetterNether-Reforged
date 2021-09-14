package itsremurin.betternetherreforged.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

public class FuelRegistry {

	private static Map<IItemProvider, Integer> FUELS = new HashMap<>();
	
	public static void onFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
		ItemStack stack = event.getItemStack();
		IItemProvider item = stack.getItem();
		if (FUELS.keySet().contains(item)) {
			event.setBurnTime(FUELS.get(item));
		}
	}
	
	public static void addFuel(IItemProvider item, int burnTime) {
		FUELS.put(item, burnTime);
	}
	
}
