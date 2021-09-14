package someoneelse.betternetherreforged.structures.decorations;

import someoneelse.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.StructureObjScatter;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.StructureWorld;

public class StructureForestLitter extends StructureObjScatter {
	private static final StructureWorld[] STRUCTURES = new StructureWorld[] {
			new StructureWorld("upside_down_forest/tree_fallen", 0, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_needle", 0, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_root", -2, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_stump", -2, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_small_branch", 0, StructureType.FLOOR)
	};

	public StructureForestLitter() {
		super(8, STRUCTURES);
	}

	@Override
	protected boolean isStructure(BlockState state) {
		return BlocksRegistry.ANCHOR_TREE.isTreeLog(state.getBlock());
	}

	@Override
	protected boolean isGround(BlockState state) {
		return BlocksHelper.isNetherGround(state);
	}
}