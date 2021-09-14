package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

public class BlockWartRoots extends BlockBase {
	public BlockWartRoots() {
		super(AbstractBlock.Properties.from(Blocks.NETHER_WART_BLOCK));
		this.setDropItself(false);
	}
}

