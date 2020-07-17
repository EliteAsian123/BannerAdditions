package eliteasian.mods.banneradditions.shield;

import com.google.gson.JsonObject;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import eliteasian.mods.banneradditions.banner.NewBannerItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ShieldRecipe extends SpecialRecipe {
    public ShieldRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof NewBannerItem) {
                    if (!bannerStack.isEmpty()) {
                        return false;
                    }

                    bannerStack = itemstack;
                } else {
                    if (itemstack.getItem() != BannerAdditionsRegistry.Items.SHIELD) {
                        return false;
                    }

                    if (!shieldStack.isEmpty()) {
                        return false;
                    }

                    if (itemstack.getChildTag("BlockEntityTag") != null) {
                        return false;
                    }

                    shieldStack = itemstack;
                }
            }
        }

        return !shieldStack.isEmpty() && !bannerStack.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack bannerStack = ItemStack.EMPTY;
        ItemStack shieldStack = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack2 = inv.getStackInSlot(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof NewBannerItem) {
                    bannerStack = itemstack2;
                } else if (itemstack2.getItem() == BannerAdditionsRegistry.Items.SHIELD) {
                    shieldStack = itemstack2.copy();
                }
            }
        }

        if (!shieldStack.isEmpty()) {
            CompoundNBT compoundnbt = bannerStack.getChildTag("BlockEntityTag");
            CompoundNBT compoundnbt1 = compoundnbt == null ? new CompoundNBT() : compoundnbt.copy();
            compoundnbt1.putInt("Base", ((NewBannerItem) bannerStack.getItem()).getColor().getId());
            shieldStack.setTagInfo("BlockEntityTag", compoundnbt1);
        }

        return shieldStack;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    public IRecipeSerializer<?> getSerializer() {
        return IRecipeSerializer.CRAFTING_SPECIAL_SHIELD;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShieldRecipe> {
        public ShieldRecipe read(ResourceLocation recipeID, JsonObject json) {
            return new ShieldRecipe(recipeID);
        }

        public ShieldRecipe read(ResourceLocation recipeID, PacketBuffer buffer) {
            return new ShieldRecipe(recipeID);
        }

        public void write(PacketBuffer buffer, ShieldRecipe recipe) {

        }
    }
}
