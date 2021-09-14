package someoneelse.betternetherreforged.items.materials;

import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import someoneelse.betternetherreforged.registry.ItemsRegistry;

public class BNItemMaterials {
	public static final IArmorMaterial CINCINNASITE_ARMOR = new BNArmorMaterial("cincinnasite", 12, 4, SoundEvents.ITEM_ARMOR_EQUIP_IRON, ItemsRegistry.CINCINNASITE_INGOT, 1F, new int[] { 3, 5, 7, 3 });
	public static final IArmorMaterial NETHER_RUBY_ARMOR = new BNArmorMaterial("nether_ruby", 13, 3, SoundEvents.ITEM_ARMOR_EQUIP_IRON, ItemsRegistry.NETHER_RUBY, 1F, new int[] { 3, 5, 7, 3 });

	public static final IItemTier CINCINNASITE_TOOLS = new BNToolMaterial(512, 6F, 2, 14, 2.5F, ItemsRegistry.CINCINNASITE_INGOT);
	public static final IItemTier CINCINNASITE_DIAMOND_TOOLS = new BNToolMaterial(1800, 8F, 3, 22, 3.5F, Items.DIAMOND);

	public static final IItemTier NETHER_RUBY_TOOLS = new BNToolMaterial(1300, 4F, 3, 9, 2.0F, ItemsRegistry.NETHER_RUBY);
}
