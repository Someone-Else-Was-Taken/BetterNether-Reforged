package itsremurin.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.BlocksHelper;


public class BlockEyeball extends BlockEyeBase {
	public BlockEyeball() {
		super(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.BROWN)
				.sound(SoundType.SLIME)
				.hardnessAndResistance(0.5F,0.5F)
				.tickRandomly());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(5) == 0) {
			double x = pos.getX() + random.nextDouble();
			double y = pos.getY() + random.nextDouble() * 0.3;
			double z = pos.getZ() + random.nextDouble();
			world.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0, 0, 0);
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextInt(64) == 0) {
			int y = BlocksHelper.downRay(world, pos, 64) + 1;
			BlockPos down = pos.down(y);
			BlockState cauldron = world.getBlockState(down);
			if (cauldron.getBlock() == Blocks.CAULDRON) {
				int level = cauldron.get(CauldronBlock.LEVEL);
				if (level < 3) {
					world.setBlockState(down, cauldron.with(CauldronBlock.LEVEL, level + 1));
				}
			}
		}
	}
}
