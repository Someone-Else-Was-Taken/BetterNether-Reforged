package someoneelse.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class BlockGeyser extends BlockBaseNotFull {
	private static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 4, 15);

	public BlockGeyser() {
		super(AbstractBlock.Properties.from(Blocks.NETHERRACK).notSolid().setLightLevel((state) -> {return 10;}));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		for (int i = 0; i < 5; i++) {
			world.addParticle(
					ParticleTypes.FLAME,
					pos.getX() + 0.4 + random.nextDouble() * 0.1,
					pos.getY() + 0.125,
					pos.getZ() + 0.4 + random.nextDouble() * 0.1,
					random.nextDouble() * 0.02 - 0.01,
					0.05D + random.nextDouble() * 0.05,
					random.nextDouble() * 0.02 - 0.01);

			world.addParticle(
					ParticleTypes.LARGE_SMOKE,
					pos.getX() + 0.4 + random.nextDouble() * 0.1,
					pos.getY() + 0.125,
					pos.getZ() + 0.4 + random.nextDouble() * 0.1,
					random.nextDouble() * 0.02 - 0.01,
					0.05D + random.nextDouble() * 0.05,
					random.nextDouble() * 0.02 - 0.01);

			world.addParticle(
					ParticleTypes.LAVA,
					pos.getX() + 0.4 + random.nextDouble() * 0.1,
					pos.getY() + 0.125 + random.nextDouble() * 0.1,
					pos.getZ() + 0.4 + random.nextDouble() * 0.1,
					random.nextDouble() * 0.02 - 0.01,
					0.05D + random.nextDouble() * 0.05,
					random.nextDouble() * 0.02 - 0.01);
		}

		if (random.nextDouble() < 0.1D) {
			world.playSound(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
		if (random.nextDouble() < 0.1D) {
			world.playSound(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
		if (random.nextDouble() < 0.1D) {
			world.playSound(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		if (!entity.isImmuneToFire() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
			entity.attackEntityFrom(DamageSource.IN_FIRE, 3F);
			entity.setFire(1);
		}

		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSolidSide(world, pos.down(), Direction.UP);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}
}

