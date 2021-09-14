package itsremurin.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import itsremurin.betternetherreforged.MHelper;

public class BlockBarrelCactus extends BlockCommonPlant implements IGrowable {
	private static final VoxelShape EMPTY = Block.makeCuboidShape(0, 0, 0, 0, 0, 0);
	private static final VoxelShape[] SHAPES = new VoxelShape[] {
			Block.makeCuboidShape(5, 0, 5, 11, 5, 11),
			Block.makeCuboidShape(3, 0, 3, 13, 9, 13),
			Block.makeCuboidShape(2, 0, 2, 14, 12, 14),
			Block.makeCuboidShape(1, 0, 1, 15, 14, 15)
	};

	public BlockBarrelCactus() {
		super(AbstractBlock.Properties.create(Material.CACTUS, MaterialColor.BLUE_TERRACOTTA)
				.sound(SoundType.CLOTH)
				.notSolid()
				.hardnessAndResistance(0.4F)
				.tickRandomly());
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Block down = world.getBlockState(pos.down()).getBlock();
		return down == Blocks.GRAVEL;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (isValidPosition(state, world, pos))
			return state;
		else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (state.get(BlockCommonPlant.AGE) > 1) entity.attackEntityFrom(DamageSource.CACTUS, 1.0F);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		Vector3d vec3d = state.getOffset(view, pos);
		return SHAPES[state.get(BlockCommonPlant.AGE)].withOffset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		if (state.get(BlockCommonPlant.AGE) < 2) return EMPTY;
		Vector3d vec3d = state.getOffset(view, pos);
		return SHAPES[state.get(BlockCommonPlant.AGE)].withOffset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XYZ;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.get(BlockCommonPlant.AGE) == 3) {
			return Lists.newArrayList(new ItemStack(this, MHelper.randRange(1, 3, MHelper.RANDOM)));
		}
		return Lists.newArrayList(new ItemStack(this));
	}
}
