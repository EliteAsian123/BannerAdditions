package eliteasian.mods.banneradditions;

import eliteasian.mods.banneradditions.banner.*;
import eliteasian.mods.banneradditions.bannerpattern.NewBannerPatternItem;
import eliteasian.mods.banneradditions.cauldron.NewCauldronBlock;
import eliteasian.mods.banneradditions.loom.NewLoomBlock;
import eliteasian.mods.banneradditions.loom.NewLoomContainer;
import eliteasian.mods.banneradditions.shield.NewShieldItem;
import eliteasian.mods.banneradditions.shield.ShieldRecipe;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public final class BannerAdditionsRegistry {
    public static class Blocks {
        public static final List<Block> BANNER_BLOCKS = new ArrayList<>();

        public static final Block WHITE_BANNER = regBanner(new NewBannerBlock(DyeColor.WHITE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "white_banner");
        public static final Block ORANGE_BANNER = regBanner(new NewBannerBlock(DyeColor.ORANGE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "orange_banner");
        public static final Block MAGENTA_BANNER = regBanner(new NewBannerBlock(DyeColor.MAGENTA, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "magenta_banner");
        public static final Block LIGHT_BLUE_BANNER = regBanner(new NewBannerBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "light_blue_banner");
        public static final Block YELLOW_BANNER = regBanner(new NewBannerBlock(DyeColor.YELLOW, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "yellow_banner");
        public static final Block LIME_BANNER = regBanner(new NewBannerBlock(DyeColor.LIME, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "lime_banner");
        public static final Block PINK_BANNER = regBanner(new NewBannerBlock(DyeColor.PINK, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "pink_banner");
        public static final Block GRAY_BANNER = regBanner(new NewBannerBlock(DyeColor.GRAY, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "gray_banner");
        public static final Block LIGHT_GRAY_BANNER = regBanner(new NewBannerBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "light_gray_banner");
        public static final Block CYAN_BANNER = regBanner(new NewBannerBlock(DyeColor.CYAN, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "cyan_banner");
        public static final Block PURPLE_BANNER = regBanner(new NewBannerBlock(DyeColor.PURPLE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "purple_banner");
        public static final Block BLUE_BANNER = regBanner(new NewBannerBlock(DyeColor.BLUE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "blue_banner");
        public static final Block BROWN_BANNER = regBanner(new NewBannerBlock(DyeColor.BROWN, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "brown_banner");
        public static final Block GREEN_BANNER = regBanner(new NewBannerBlock(DyeColor.GREEN, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "green_banner");
        public static final Block RED_BANNER = regBanner(new NewBannerBlock(DyeColor.RED, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "red_banner");
        public static final Block BLACK_BANNER = regBanner(new NewBannerBlock(DyeColor.BLACK, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "black_banner");

        public static final Block WHITE_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.WHITE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(WHITE_BANNER)), "white_wall_banner");
        public static final Block ORANGE_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.ORANGE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(ORANGE_BANNER)), "orange_wall_banner");
        public static final Block MAGENTA_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.MAGENTA, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(MAGENTA_BANNER)), "magenta_wall_banner");
        public static final Block LIGHT_BLUE_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(LIGHT_BLUE_BANNER)), "light_blue_wall_banner");
        public static final Block YELLOW_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.YELLOW, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(YELLOW_BANNER)), "yellow_wall_banner");
        public static final Block LIME_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.LIME, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(LIME_BANNER)), "lime_wall_banner");
        public static final Block PINK_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.PINK, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(PINK_BANNER)), "pink_wall_banner");
        public static final Block GRAY_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.GRAY, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(GRAY_BANNER)), "gray_wall_banner");
        public static final Block LIGHT_GRAY_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(LIGHT_GRAY_BANNER)), "light_gray_wall_banner");
        public static final Block CYAN_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.CYAN, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(CYAN_BANNER)), "cyan_wall_banner");
        public static final Block PURPLE_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.PURPLE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(PURPLE_BANNER)), "purple_wall_banner");
        public static final Block BLUE_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.BLUE, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(BLUE_BANNER)), "blue_wall_banner");
        public static final Block BROWN_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.BROWN, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(BROWN_BANNER)), "brown_wall_banner");
        public static final Block GREEN_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.GREEN, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(GREEN_BANNER)), "green_wall_banner");
        public static final Block RED_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.RED, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(RED_BANNER)), "red_wall_banner");
        public static final Block BLACK_WALL_BANNER = regBanner(new NewWallBannerBlock(DyeColor.BLACK, AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(BLACK_BANNER)), "black_wall_banner");

        public static final Block LOOM = reg(new NewLoomBlock(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD)), "loom");

        public static final Block CAULDRON = reg(new NewCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE).func_235861_h_().hardnessAndResistance(2.0F).notSolid()), "cauldron");

        private static Block regBanner(Block block, String name) {
            block.setRegistryName(new ResourceLocation("minecraft", name));
            BANNER_BLOCKS.add(block);
            return block;
        }

        private static Block reg(Block block, String name) {
            return block.setRegistryName(new ResourceLocation("minecraft", name));
        }

        public static void registerAllBlocks(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(BANNER_BLOCKS.toArray(new Block[0]));

            event.getRegistry().register(LOOM);
            event.getRegistry().register(CAULDRON);
        }
    }

    public static class TileEntities {
        public static final TileEntityType<NewBannerTileEntity> BANNER = reg(TileEntityType.Builder.create(NewBannerTileEntity::new, BannerAdditionsRegistry.Blocks.BANNER_BLOCKS.toArray(new Block[0])), "banner");

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
        public static final List<Item> BANNER_ITEMS = new ArrayList<>();

        public static final Item WHITE_BANNER = regBanner(new NewBannerItem(Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "white_banner");
        public static final Item ORANGE_BANNER = regBanner(new NewBannerItem(Blocks.ORANGE_BANNER, Blocks.ORANGE_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "orange_banner");
        public static final Item MAGENTA_BANNER = regBanner(new NewBannerItem(Blocks.MAGENTA_BANNER, Blocks.MAGENTA_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "magenta_banner");
        public static final Item LIGHT_BLUE_BANNER = regBanner(new NewBannerItem(Blocks.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "light_blue_banner");
        public static final Item YELLOW_BANNER = regBanner(new NewBannerItem(Blocks.YELLOW_BANNER, Blocks.YELLOW_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "yellow_banner");
        public static final Item LIME_BANNER = regBanner(new NewBannerItem(Blocks.LIME_BANNER, Blocks.LIME_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "lime_banner");
        public static final Item PINK_BANNER = regBanner(new NewBannerItem(Blocks.PINK_BANNER, Blocks.PINK_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "pink_banner");
        public static final Item GRAY_BANNER = regBanner(new NewBannerItem(Blocks.GRAY_BANNER, Blocks.GRAY_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "gray_banner");
        public static final Item LIGHT_GRAY_BANNER = regBanner(new NewBannerItem(Blocks.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "light_gray_banner");
        public static final Item CYAN_BANNER = regBanner(new NewBannerItem(Blocks.CYAN_BANNER, Blocks.CYAN_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "cyan_banner");
        public static final Item PURPLE_BANNER = regBanner(new NewBannerItem(Blocks.PURPLE_BANNER, Blocks.PURPLE_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "purple_banner");
        public static final Item BLUE_BANNER = regBanner(new NewBannerItem(Blocks.BLUE_BANNER, Blocks.BLUE_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "blue_banner");
        public static final Item BROWN_BANNER = regBanner(new NewBannerItem(Blocks.BROWN_BANNER, Blocks.BROWN_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "brown_banner");
        public static final Item GREEN_BANNER = regBanner(new NewBannerItem(Blocks.GREEN_BANNER, Blocks.GREEN_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "green_banner");
        public static final Item RED_BANNER = regBanner(new NewBannerItem(Blocks.RED_BANNER, Blocks.RED_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "red_banner");
        public static final Item BLACK_BANNER = regBanner(new NewBannerItem(Blocks.BLACK_BANNER, Blocks.BLACK_WALL_BANNER, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS)), "black_banner");

        public static final Item LOOM = reg(new BlockItem(BannerAdditionsRegistry.Blocks.LOOM, (new Item.Properties()).group(ItemGroup.DECORATIONS)), "loom");

        public static final Item CAULDRON = reg(new BlockItem(BannerAdditionsRegistry.Blocks.CAULDRON, (new Item.Properties()).group(ItemGroup.BREWING)), "cauldron");

        public static final Item SHIELD = reg(new NewShieldItem((new Item.Properties()).maxDamage(336).group(ItemGroup.COMBAT)), "shield");

        public static final Item GLOBE_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "globe_banner_pattern");
        public static final Item CREEPER_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "creeper_banner_pattern");
        public static final Item SKULL_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "skull_banner_pattern");
        public static final Item FLOWER_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "flower_banner_pattern");
        public static final Item MOJANG_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "mojang_banner_pattern");
        public static final Item PIGLIN_BANNER_PATTERN = reg(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "piglin_banner_pattern");

        public static final Item SWORD_BANNER_PATTERN = regMod(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "sword_banner_pattern");
        public static final Item ANIMATED_BANNER_PATTERN = regMod(new NewBannerPatternItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)), "animated_banner_pattern");

        private static Item regBanner(Item item, String name) {
            item.setRegistryName(new ResourceLocation("minecraft", name));
            BANNER_ITEMS.add(item);
            return item;
        }

        private static Item reg(Item item, String name) {
            return item.setRegistryName(new ResourceLocation("minecraft", name));
        }

        private static Item regMod(Item item, String name) {
            return item.setRegistryName(new ResourceLocation(BannerAdditions.MOD_ID, name));
        }

        public static void registerAllItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(BANNER_ITEMS.toArray(new Item[0]));

            event.getRegistry().registerAll(
                    LOOM,
                    CAULDRON,

                    SHIELD,

                    GLOBE_BANNER_PATTERN,
                    CREEPER_BANNER_PATTERN,
                    SKULL_BANNER_PATTERN,
                    FLOWER_BANNER_PATTERN,
                    MOJANG_BANNER_PATTERN,
                    PIGLIN_BANNER_PATTERN,

                    SWORD_BANNER_PATTERN,
                    ANIMATED_BANNER_PATTERN
            );
        }
    }

    public static class CraftingRecipes {
        public static final IRecipeSerializer<ShieldRecipe> SHIELD_RECIPE = reg(new ShieldRecipe.Serializer(), "shield_recipe");

        private static <T extends ICraftingRecipe> IRecipeSerializer<T> reg(IRecipeSerializer<T> recipeSerializer, String name) {
            return (IRecipeSerializer<T>) recipeSerializer.setRegistryName(new ResourceLocation(BannerAdditions.MOD_ID, name));
        }

        public static void registerAllRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            event.getRegistry().register(SHIELD_RECIPE);
        }
    }
}