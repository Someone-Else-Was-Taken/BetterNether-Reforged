package itsremurin.betternetherreforged.mixin;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.ChunkSerializer;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
	@Overwrite
	private static CompoundNBT writeStructures(ChunkPos pos, Map<Structure<?>, StructureStart<?>> structureStarts, Map<Structure<?>, LongSet> structureReferences) {
		CompoundNBT tagResult = new CompoundNBT();
		CompoundNBT tagStarts = new CompoundNBT();
		Iterator<Entry<Structure<?>, StructureStart<?>>> startsIterator = structureStarts.entrySet().iterator();

		while (startsIterator.hasNext()) {
			Entry<Structure<?>, StructureStart<?>> start = startsIterator.next();
			tagStarts.put((start.getKey()).getStructureName(), (start.getValue()).write(pos.x, pos.z));
		}

		tagResult.put("Starts", tagStarts);
		CompoundNBT tagReferences = new CompoundNBT();
		Iterator<Entry<Structure<?>, LongSet>> refIterator = structureReferences.entrySet().iterator();

		while (refIterator.hasNext()) {
			Entry<Structure<?>, LongSet> feature = refIterator.next();
			// Structures sometimes can be null
			if (feature.getKey() != null) {
				tagReferences.put((feature.getKey()).getStructureName(), new LongArrayNBT(feature.getValue()));
			}
		}

		tagResult.put("References", tagReferences);
		return tagResult;
	}
}
