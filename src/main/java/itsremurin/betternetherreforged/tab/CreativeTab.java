package itsremurin.betternetherreforged.tab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import itsremurin.betternetherreforged.BetterNether;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class CreativeTab {
    public static final ItemGroup BN_TAB = new ItemGroup(BetterNether.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BlocksRegistry.NETHER_GRASS);
        }
    };
}
