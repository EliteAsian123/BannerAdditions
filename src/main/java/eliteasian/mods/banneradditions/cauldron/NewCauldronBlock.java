package eliteasian.mods.banneradditions.cauldron;

import eliteasian.mods.banneradditions.banner.NewBannerItem;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class NewCauldronBlock extends CauldronBlock {
    public NewCauldronBlock(Properties properties) {
        super(properties);
    }

    // Yes, I have to replace this whole method for one thing.
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (itemstack.isEmpty()) {
            return ActionResultType.PASS;
        } else {
            int i = state.get(LEVEL);
            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET) {
                if (i < 3 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    }

                    player.addStat(Stats.FILL_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 3);
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.BUCKET) {
                if (i == 3 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                            player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    player.addStat(Stats.USE_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 0);
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.GLASS_BOTTLE) {
                if (i > 0 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        ItemStack itemstack4 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);
                        player.addStat(Stats.USE_CAULDRON);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, itemstack4);
                        } else if (!player.inventory.addItemStackToInventory(itemstack4)) {
                            player.dropItem(itemstack4, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                        }
                    }

                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setWaterLevel(worldIn, pos, state, i - 1);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.POTION && PotionUtils.getPotionFromItem(itemstack) == Potions.WATER) {
                if (i < 3 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                        player.addStat(Stats.USE_CAULDRON);
                        player.setHeldItem(handIn, itemstack3);
                        if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                        }
                    }

                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setWaterLevel(worldIn, pos, state, i + 1);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else {
                if (i > 0 && item instanceof IDyeableArmorItem) {
                    IDyeableArmorItem idyeablearmoritem = (IDyeableArmorItem)item;
                    if (idyeablearmoritem.hasColor(itemstack) && !worldIn.isRemote) {
                        idyeablearmoritem.removeColor(itemstack);
                        this.setWaterLevel(worldIn, pos, state, i - 1);
                        player.addStat(Stats.CLEAN_ARMOR);
                        return ActionResultType.SUCCESS;
                    }
                }

                if (i > 0 && item instanceof NewBannerItem) {
                    if (NewBannerTileEntity.getPatterns(itemstack) > 0 && !worldIn.isRemote) {
                        ItemStack itemstack2 = itemstack.copy();
                        itemstack2.setCount(1);
                        NewBannerTileEntity.removeBannerData(itemstack2);
                        player.addStat(Stats.CLEAN_BANNER);
                        if (!player.abilities.isCreativeMode) {
                            itemstack.shrink(1);
                            this.setWaterLevel(worldIn, pos, state, i - 1);
                        }

                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, itemstack2);
                        } else if (!player.inventory.addItemStackToInventory(itemstack2)) {
                            player.dropItem(itemstack2, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                        }
                    }

                    return ActionResultType.func_233537_a_(worldIn.isRemote);
                } else if (i > 0 && item instanceof BlockItem) {
                    Block block = ((BlockItem)item).getBlock();
                    if (block instanceof ShulkerBoxBlock && !worldIn.isRemote()) {
                        ItemStack itemstack1 = new ItemStack(Blocks.SHULKER_BOX, 1);
                        if (itemstack.hasTag()) {
                            itemstack1.setTag(itemstack.getTag().copy());
                        }

                        player.setHeldItem(handIn, itemstack1);
                        this.setWaterLevel(worldIn, pos, state, i - 1);
                        player.addStat(Stats.CLEAN_SHULKER_BOX);
                        return ActionResultType.SUCCESS;
                    } else {
                        return ActionResultType.CONSUME;
                    }
                } else {
                    return ActionResultType.PASS;
                }
            }
        }
    }
}
