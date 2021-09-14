package someoneelse.betternetherreforged.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import someoneelse.betternetherreforged.tab.CreativeTab;

public class ItemBlackApple extends Item {
	public static final Food BLACK_APPLE = new Food.Builder().hunger(6).saturation(0.5F).build();

	public ItemBlackApple() {
		super(new Item.Properties()
				.group(CreativeTab.BN_TAB)
				.food(BLACK_APPLE)
				.food(Foods.APPLE));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity user) {
		user.addPotionEffect(new EffectInstance(Effects.REGENERATION, 60, 1));
		return super.onItemUseFinish(stack, world, user);
	}
}
