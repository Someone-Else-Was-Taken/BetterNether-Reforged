package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockBNPot extends BlockBaseNotFull {
	private static final VoxelShape SHAPE = makeCuboidShape(3, 0, 3, 13, 8, 13);

	public BlockBNPot(Block material) {
		super(AbstractBlock.Properties.from(material).notSolid());
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		BlockPos plantPos = pos.up();
		if (hit.getFace() == Direction.UP && world.isAirBlock(plantPos)) {
			BlockState plant = BlockPottedPlant.getPlant(player.getHeldItemMainhand().getItem());
			if (plant != null) {
				if (!world.isRemote())
					BlocksHelper.setWithUpdate((ServerWorld) world, plantPos, plant);
				world.playSound(
						pos.getX() + 0.5,
						pos.getY() + 1.5,
						pos.getZ() + 0.5,
						SoundEvents.ITEM_CROP_PLANT,
						SoundCategory.BLOCKS,
						0.8F,
						1.0F,
						true);
				if (!player.isCreative())
					player.getHeldItemMainhand().shrink(1);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}
}

