package itsremurin.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockEyeBase extends BlockBase {
	public BlockEyeBase(AbstractBlock.Properties settings) {
		super(settings.setAllowsSpawn((state, world, pos, type) -> {
			return false;
		}));
		setDropItself(false);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		BlockPos blockPos = pos.up();
		Block up = world.getBlockState(blockPos).getBlock();
		if (up != BlocksRegistry.EYE_VINE && up != Blocks.NETHERRACK)
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.EYE_SEED);
	}
}
