package eliteasian.mods.banneradditions;

import eliteasian.mods.banneradditions.banner.*;
import eliteasian.mods.banneradditions.bannerpattern.NewBannerPatternItem;
import eliteasian.mods.banneradditions.loom.NewLoomContainer;
import eliteasian.mods.banneradditions.banner.ShieldRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class BannerAdditionsRegistry {
    public static class TileEntities {
        public static final TileEntityType<NewBannerTileEntity> BANNER = reg(TileEntityType.Builder.create(NewBannerTileEntity::new, Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER, Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER), "banner");

        private static <T extends TileEntity> TileEntityType<T> reg(TileEntityType.Builder<T> builder, String name) {
            return (TileEntityType<T>) builder.build(null).setRegistryName(new ResourceLocation("minecraft", name));
        }

        public static void registerAllTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(BANNER);
        }
    }

    public static class Containers {
        public static final ContainerType<NewLoomContainer> LOOM = reg(NewLoomContainer::new, "loom");

        private static <T extends Container> ContainerType<T> reg(ContainerType.IFactory<T> factory, String name) {
            return (ContainerType<T>) new ContainerType<>(factory).setRegistryName(new ResourceLocation("minecraft", name));
        }

        public static void registerAllContainers(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(LOOM);
        }
    }

    public static class Items {
        public static final Item SWORD_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "sword_banner_pattern");
        public static final Item ANIMATED_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "animated_banner_pattern");

        private static Item reg(Item item, String name) {
            return item.setRegistryName(new ResourceLocation(BannerAdditions.MOD_ID, name));
        }

        public static void registerAllItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    SWORD_BANNER_PATTERN,
                    ANIMATED_BANNER_PATTERN
            );
        }
    }

    public static class CraftingRecipes {
        public static final IRecipeSerializer<ShieldRecipe> SHIELD_RECIPE = reg(new ShieldRecipe.Serializer(), "shield_recipe");
        public static final IRecipeSerializer<BannerCopyRecipe> BANNER_COPY_RECIPE = reg(new BannerCopyRecipe.Serializer(), "banner_copy_recipe");

        private static <T extends ICraftingRecipe> IRecipeSerializer<T> reg(IRecipeSerializer<T> recipeSerializer, String name) {
            return (IRecipeSerializer<T>) recipeSerializer.setRegistryName(new ResourceLocation(BannerAdditions.MOD_ID, name));
        }

        public static void registerAllRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            event.getRegistry().registerAll(SHIELD_RECIPE, BANNER_COPY_RECIPE);
        }
    }
}