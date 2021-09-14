package someoneelse.betternetherreforged.structures.plants;

import someoneelse.betternetherreforged.blocks.BlockCommonPlant;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureFeatherFern extends StructureScatter implements IStructure {
	public StructureFeatherFern() {
		super(BlocksRegistry.FEATHER_FERN, BlockCommonPlant.AGE, 4);
	}
}