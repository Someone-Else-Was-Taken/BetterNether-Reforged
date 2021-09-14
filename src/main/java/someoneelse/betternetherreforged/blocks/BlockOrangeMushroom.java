package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockOrangeMushroom extends BlockCommonPlant {
	private static final VoxelShape[] SHAPES = new VoxelShape[] {
			VoxelShapes.create(0.25, 0.0, 0.25, 0.75, 0.375, 0.75),
			VoxelShapes.create(0.125, 0.0, 0.125, 0.875, 0.625, 0.875),
			VoxelShapes.create(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375),
			VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
	};

	public BlockOrangeMushroom() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.ADOBE)
				.sound(SoundType.CROP)
				.notSolid()
				.hardnessAndResistance(0.5F)
				.tickRandomly()
				.doesNotBlockMovement());
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return BlocksHelper.isNetherMycelium(world.getBlockState(pos.down()));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPES[state.get(AGE)];
	}
}