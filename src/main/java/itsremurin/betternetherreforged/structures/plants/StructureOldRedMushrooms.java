package itsremurin.betternetherreforged.structures.plants;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.StructureObjScatter;
import itsremurin.betternetherreforged.structures.StructureType;
import itsremurin.betternetherreforged.structures.StructureWorld;

public class StructureOldRedMushrooms extends StructureObjScatter {
	private static final StructureWorld[] TREES = new StructureWorld[] {
			new StructureWorld("trees/red_mushroom_01", -2, StructureType.FLOOR),
			new StructureWorld("trees/red_mushroom_02", -1, StructureType.FLOOR),
			new StructureWorld("trees/red_mushroom_03", -1, StructureType.FLOOR),
			new StructureWorld("trees/red_mushroom_04", -4, StructureType.FLOOR),
			new StructureWorld("trees/red_mushroom_05", -4, StructureType.FLOOR),
			new StructureWorld("trees/red_mushroom_06", -1, StructureType.FLOOR),
			new StructureWorld("trees/red_mushroom_07", -4, StructureType.FLOOR)
	};

	public StructureOldRedMushrooms() {
		super(9, TREES);
	}

	protected boolean isGround(BlockState state) {
		return state.getBlock() == BlocksRegistry.NETHER_MYCELIUM || BlocksHelper.isNetherGround(state);
	}

	protected boolean isStructure(BlockState state) {
		return state.getBlock() == Blocks.MUSHROOM_STEM ||
				state.getBlock() == Blocks.BROWN_MUSHROOM_BLOCK ||
				state.getBlock() == Blocks.RED_MUSHROOM_BLOCK;
	}
}
