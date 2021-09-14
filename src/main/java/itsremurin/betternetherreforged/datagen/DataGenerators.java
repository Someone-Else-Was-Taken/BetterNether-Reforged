package itsremurin.betternetherreforged.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class DataGenerators {
	public static void gatherData(GatherDataEvent event) {
		if(event.includeServer()) {
			DataGenerator generator = event.getGenerator();
			generator.addProvider(new BNRecipes(generator));
		}
	}
}
