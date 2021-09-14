package itsremurin.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEyeballSmall extends BlockEyeBase {
	protected static final VoxelShape SHAPE = makeCuboidShape(4, 8, 4, 12, 16, 12);

	public BlockEyeballSmall() {
		super(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.BROWN)
				.sound(SoundType.SLIME)
				.hardnessAndResistance(0.5F, 0.5F)
				.notSolid());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(5) == 0) {
			double x = pos.getX() + random.nextDouble() * 0.5 + 0.25;
			double y = pos.getY() + random.nextDouble() * 0.1 + 0.5;
			double z = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
			world.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0, 0, 0);
		}
	}
}

