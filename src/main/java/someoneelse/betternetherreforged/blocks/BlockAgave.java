package someoneelse.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.ItemsRegistry;

public class BlockAgave extends BlockCommonPlant {
	private static final VoxelShape SHAPE = makeCuboidShape(2, 0, 2, 14, 14, 14);

	public BlockAgave() {
		super(AbstractBlock.Properties.create(Material.CACTUS, MaterialColor.ORANGE_TERRACOTTA)
				.sound(SoundType.CLOTH)
				.notSolid()
				.doesNotBlockMovement()
				.hardnessAndResistance(0.4F)
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		Vector3d vec3d = state.getOffset(view, pos);
		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XZ;
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
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.get(BlockCommonPlant.AGE) == 3) {
			return Lists.newArrayList(new ItemStack(this, MHelper.randRange(1, 2, MHelper.RANDOM)), new ItemStack(ItemsRegistry.AGAVE_LEAF, MHelper.randRange(2, 5, MHelper.RANDOM)));
		}
		return Lists.newArrayList(new ItemStack(this));
	}
}

