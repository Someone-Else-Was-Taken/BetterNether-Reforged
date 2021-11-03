package someoneelse.betternetherreforged.mixin;

import someoneelse.betternetherreforged.world.NetherBiomeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;

@Mixin(value = DimensionType.class, priority = 100)
public class DimensionTypeMixin {
	@Inject(method = "getNetherChunkGenerator(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/Registry;J)Lnet/minecraft/world/gen/ChunkGenerator;", at = @At("HEAD"), cancellable = true)
	private static void replaceGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> chunkGeneratorSettingsRegistry, long seed, CallbackInfoReturnable<ChunkGenerator> info) {
		info.setReturnValue(new NoiseChunkGenerator(
				new NetherBiomeProvider(biomeRegistry, seed), seed,
				() -> chunkGeneratorSettingsRegistry.getOrThrow(DimensionSettings.field_242736_e)));

	}
}
