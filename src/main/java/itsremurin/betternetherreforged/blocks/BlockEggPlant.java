package itsremurin.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import itsremurin.betternetherreforged.config.Configs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import itsremurin.betternetherreforged.registry.EntityRegistry;

public class BlockEggPlant extends BlockCommonPlant {
	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 8, 16);
	public static final BooleanProperty DESTRUCTED = BooleanProperty.create("destructed");

	private boolean enableModDamage = true;
	private boolean enablePlayerDamage = true;

	public BlockEggPlant() {
		super(MaterialColor.WHITE_TERRACOTTA);
		enableModDamage = Configs.MAIN.getBoolean("egg_plant", "mob_damage", true);
		enablePlayerDamage = Configs.MAIN.getBoolean("egg_plant", "player_damage", true);
		this.setDefaultState(getStateContainer().getBaseState().with(DESTRUCTED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		super.fillStateContainer(stateManager);
		stateManager.add(DESTRUCTED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!state.get(DESTRUCTED))
			world.addParticle(
					ParticleTypes.ENTITY_EFFECT,
					pos.getX() + random.nextDouble(),
					pos.getY() + 0.4,
					pos.getZ() + random.nextDouble(),
					0.46, 0.28, 0.55);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!state.get(DESTRUCTED)) {
			if (enableModDamage && entity instanceof LivingEntity && !((LivingEntity) entity).isPotionActive(Effects.POISON)) {
				if (!EntityRegistry.isNetherEntity(entity))
					((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 100, 3));
			}
			else if (enablePlayerDamage && entity instanceof PlayerEntity && !((PlayerEntity) entity).isPotionActive(Effects.POISON))
				((PlayerEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 100, 3));

			double px = pos.getX() + 0.5;
			double py = pos.getY() + 0.125;
			double pz = pos.getZ() + 0.5;
			if (world.isRemote) {
				world.playSound(px, py, pz, SoundType.WART.getBreakSound(), SoundCategory.BLOCKS, 1, 1, false);
				BlockParticleData effect = new BlockParticleData(ParticleTypes.BLOCK, state);
				Random random = world.rand;
				for (int i = 0; i < 24; i++)
					world.addParticle(effect,
							px + random.nextGaussian() * 0.2,
							py + random.nextGaussian() * 0.2,
							pz + random.nextGaussian() * 0.2,
							random.nextGaussian(),
							random.nextGaussian(),
							random.nextGaussian());
			}

			world.setBlockState(pos, state.with(DESTRUCTED, true));
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (builder.get(LootParameters.TOOL).getItem().isIn(Tags.Items.SHEARS))
			return Collections.singletonList(new ItemStack(this.asItem()));
		else
			return super.getDrops(state, builder);
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		if (state.get(DESTRUCTED))
			world.setBlockState(pos, this.getDefaultState());
	}
}
