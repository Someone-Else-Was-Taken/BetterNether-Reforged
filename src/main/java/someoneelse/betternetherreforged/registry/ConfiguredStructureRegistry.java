package someoneelse.betternetherreforged.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.world.structures.CityFeature;

public class ConfiguredStructureRegistry {
    /**
     * Static instance of our structure so we can reference it and add it to biomes easily.
     */
    public static final StructureFeature<?, ?> CONFIGURED_CITY = CityRegistry.CITY.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     * <p>
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(registry, new ResourceLocation(BetterNether.MOD_ID, "configured_city"), CONFIGURED_CITY);}
}