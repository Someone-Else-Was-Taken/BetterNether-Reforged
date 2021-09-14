package itsremurin.betternetherreforged.tileentities;

import java.util.List;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.blocks.BlockChestOfDrawers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import itsremurin.betternetherreforged.registry.TileEntitiesRegistry;

public class TileEntityChestOfDrawers extends LockableLootTileEntity {
	private NonNullList<ItemStack> inventory;
	private int watchers = 0;

	public TileEntityChestOfDrawers() {
		super(TileEntitiesRegistry.CHEST_OF_DRAWERS);
		this.inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.chest_of_drawers", new Object[0]);
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return ChestContainer.createGeneric9X3(syncId, playerInventory, this);
	}

	@Override
	public void read(BlockState blockState, CompoundNBT tag) {
		super.read(blockState, tag);
		this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if (!this.checkLootAndRead(tag)) {
			ItemStackHelper.loadAllItems(tag, this.inventory);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);
		if (!this.checkLootAndWrite(tag)) {
			ItemStackHelper.saveAllItems(tag, this.inventory);
		}
		return tag;
	}

	public void onInvOpen(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.watchers < 0) {
				this.watchers = 0;
			}
			if (this.watchers == 0) {
				this.playSound(this.getBlockState(), SoundEvents.BLOCK_BARREL_OPEN);
			}

			++this.watchers;
			this.onInvOpenOrClose();
		}
	}

	@Override
	public void closeInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			--this.watchers;
			this.onInvOpenOrClose();
		}
	}

	protected void onInvOpenOrClose() {
		BlockState state = this.getBlockState();
		Block block = state.getBlock();
		if (block instanceof BlockChestOfDrawers && !world.isRemote) {
			if (watchers > 0 && !state.get(BlockChestOfDrawers.OPEN)) {
				BlocksHelper.setWithoutUpdate((ServerWorld) world, pos, state.with(BlockChestOfDrawers.OPEN, true));
			}
			else if (watchers == 0 && state.get(BlockChestOfDrawers.OPEN)) {
				BlocksHelper.setWithoutUpdate((ServerWorld) world, pos, state.with(BlockChestOfDrawers.OPEN, false));
			}
		}
	}

	private void playSound(BlockState blockState, SoundEvent soundEvent) {
		Vector3i vec3i = ((Direction) blockState.get(BlockChestOfDrawers.FACING)).getDirectionVec();
		double d = (double) this.pos.getX() + 0.5D + (double) vec3i.getX() / 2.0D;
		double e = (double) this.pos.getY() + 0.5D + (double) vec3i.getY() / 2.0D;
		double f = (double) this.pos.getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
		this.world.playSound((PlayerEntity) null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	public void addItemsToList(List<ItemStack> items) {
		for (ItemStack item : inventory)
			if (item != null)
				items.add(item);
	}
}

