package someoneelse.betternetherreforged.structures.plants;

import someoneelse.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.StructureObjScatter;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.StructureWorld;

public class StructureOldBrownMushrooms extends StructureObjScatter {
	private static final StructureWorld[] TREES = new StructureWorld[] {
			new StructureWorld("trees/brown_mushroom_01", -4, StructureType.FLOOR),
			new StructureWorld("trees/brown_mushroom_02", -3, StructureType.FLOOR),
			new StructureWorld("trees/brown_mushroom_03", -3, StructureType.FLOOR),
			new StructureWorld("trees/brown_mushroom_04", -2, StructureType.FLOOR)
	};

	public StructureOldBrownMushrooms() {
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
