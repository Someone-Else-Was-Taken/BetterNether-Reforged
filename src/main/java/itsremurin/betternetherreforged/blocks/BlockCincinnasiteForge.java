package itsremurin.betternetherreforged.blocks;

import java.util.Random;
import java.util.function.ToIntFunction;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.tileentities.TileEntityForge;


public class BlockCincinnasiteForge extends AbstractFurnaceBlock {
	public BlockCincinnasiteForge() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK).setRequiresTool().setLightLevel(getLuminance()));
	}

	private static ToIntFunction<BlockState> getLuminance() {
		return (blockState) -> {
			return (Boolean) blockState.get(BlockStateProperties.LIT) ? 13 : 0;
		};
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityForge();
	}

	@Override
	protected void interactWith(World world, BlockPos pos, PlayerEntity player) {
		TileEntity blockEntity = world.getTileEntity(pos);
		if (blockEntity instanceof TileEntityForge) {
			player.openContainer((INamedContainerProvider) blockEntity);
			player.addStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if ((Boolean) state.get(LIT)) {
			double d = (double) pos.getX() + 0.5D;
			double e = (double) pos.getY();
			double f = (double) pos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = (Direction) state.get(FACING);
			Direction.Axis axis = direction.getAxis();
			double h = random.nextDouble() * 0.6D - 0.3D;
			double i = axis == Direction.Axis.X ? (double) direction.getYOffset() * 0.52D : h;
			double j = random.nextDouble() * 6.0D / 16.0D;
			double k = axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : h;
			world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
		}
	}
}
