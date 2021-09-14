package itsremurin.betternetherreforged.blocks;

import java.util.Random;
import java.util.function.ToIntFunction;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class BlockFireBowl extends BlockBaseNotFull {
	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 12, 16);
	public static final BooleanProperty FIRE = BooleanProperty.create("fire");

	public BlockFireBowl(Block source) {
		super(AbstractBlock.Properties.from(source).notSolid().setLightLevel(getLuminance()));
		this.setDefaultState(getStateContainer().getBaseState().with(FIRE, false));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	protected static ToIntFunction<BlockState> getLuminance() {
		return (state) -> {
			return state.get(FIRE) ? 15 : 0;
		};
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(FIRE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (hit.getFace() == Direction.UP) {
			if (player.getHeldItemMainhand().getItem() == Items.FLINT_AND_STEEL && !state.get(FIRE)) {
				world.setBlockState(pos, state.with(FIRE, true));
				if (!player.isCreative() && !world.isRemote)
					player.getHeldItemMainhand().attemptDamageItem(1, world.rand, (ServerPlayerEntity) player);
				world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
				return ActionResultType.SUCCESS;
			}
			else if (player.getHeldItemMainhand().isEmpty() && state.get(FIRE)) {
				world.setBlockState(pos, state.with(FIRE, false));
				world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		if (!entity.isImmuneToFire() && entity instanceof LivingEntity && world.getBlockState(pos).get(FIRE)) {
			entity.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(FIRE)) {
			if (random.nextInt(24) == 0)
				world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			if (random.nextInt(4) == 0)
				world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + random.nextDouble(), pos.getY() + 0.75, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}
}