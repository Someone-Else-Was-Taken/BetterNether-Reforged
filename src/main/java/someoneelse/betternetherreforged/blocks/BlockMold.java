package someoneelse.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockMold extends BlockBaseNotFull {
	public BlockMold(MaterialColor color) {
		super(MaterialBuilder.makeGrass(color)
				.sound(SoundType.CROP)
				.notSolid()
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
	}

	public BlockMold(AbstractBlock.Properties settings) {
		super(settings);
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader view, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XZ;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return BlocksHelper.isNetherMycelium(world.getBlockState(pos.down()));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (random.nextInt(16) == 0) {
			int c = 0;
			c = world.getBlockState(pos.north()).getBlock() == this ? c++ : c;
			c = world.getBlockState(pos.south()).getBlock() == this ? c++ : c;
			c = world.getBlockState(pos.east()).getBlock() == this ? c++ : c;
			c = world.getBlockState(pos.west()).getBlock() == this ? c++ : c;
			if (c < 2) {
				BlockPos npos = new BlockPos(pos);
				switch (random.nextInt(4)) {
					case 0:
						npos = npos.add(-1, 0, 0);
						break;
					case 1:
						npos = npos.add(1, 0, 0);
						break;
					case 2:
						npos = npos.add(0, 0, -1);
						break;
					default:
						npos = npos.add(0, 0, 1);
						break;
				}
				if (world.isAirBlock(npos) && isValidPosition(state, world, npos)) {
					BlocksHelper.setWithoutUpdate(world, npos, getDefaultState());
				}
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (builder.get(LootParameters.TOOL).getItem() instanceof ShearsItem)
			return Collections.singletonList(new ItemStack(this.asItem()));
		else
			return super.getDrops(state, builder);
	}
}
