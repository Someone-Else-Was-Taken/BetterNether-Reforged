package itsremurin.betternetherreforged.structures.plants;

import itsremurin.betternetherreforged.blocks.BlockCommonPlant;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureFeatherFern extends StructureScatter implements IStructure {
	public StructureFeatherFern() {
		super(BlocksRegistry.FEATHER_FERN, BlockCommonPlant.AGE, 4);
	}
}