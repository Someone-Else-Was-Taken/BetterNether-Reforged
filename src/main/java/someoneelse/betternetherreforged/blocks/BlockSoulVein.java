package someoneelse.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockSoulVein extends BlockBaseNotFull implements IGrowable {
	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 1, 16);

	public BlockSoulVein() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.PURPLE)
				.sound(SoundType.CROP)
				.notSolid()
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		return block == Blocks.SOUL_SAND || block == BlocksRegistry.VEINED_SAND;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (isValidPosition(state, world, pos))
			return state;
		else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		spawnAsEntity(world, pos, new ItemStack(this.asItem()));
	}

	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockState(pos.down()).getBlock() == Blocks.SOUL_SAND)
			world.setBlockState(pos.down(), BlocksRegistry.VEINED_SAND.getDefaultState());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (builder.get(LootParameters.TOOL).getItem().isIn(Tags.Items.SHEARS))
			return Collections.singletonList(new ItemStack(this.asItem()));
		else
			return super.getDrops(state, builder);
	}
}

