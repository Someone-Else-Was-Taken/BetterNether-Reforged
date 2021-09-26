package someoneelse.betternetherreforged.mixin;

import java.util.List;
import java.util.concurrent.Executor;

import someoneelse.betternetherreforged.world.structures.CityFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.SaveFormat;
import someoneelse.betternetherreforged.world.BNWorldGenerator;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "<init>*", at = @At("RETURN"))
	private void onInit(MinecraftServer server, Executor workerExecutor, SaveFormat.LevelSave session, IServerWorldInfo properties, RegistryKey<World> registryKey, DimensionType dimensionType,
			IChunkStatusListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean bl, long seed, List<ISpecialSpawner> list, boolean bl2, CallbackInfo info) {
		BNWorldGenerator.init(seed);
		CityFeature.initGenerator();
	}
}