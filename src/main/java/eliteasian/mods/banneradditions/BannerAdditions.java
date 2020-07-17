package eliteasian.mods.banneradditions;

import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntityRenderer;
import eliteasian.mods.banneradditions.loom.NewLoomScreen;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BannerAdditions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BannerAdditions {
    public static BannerAdditions instance;

    public static final String MOD_ID = "banneradditions";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final int MAX_PATTERN_COUNT = 12;

    public BannerAdditions() {
        instance = this;

        FMLJavaModLoadingContext modLoader = FMLJavaModLoadingContext.get();

        modLoader.getModEventBus().addListener(this::setup);
        modLoader.getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(BannerPatterns.INSTANCE);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(BannerAdditionsRegistry.TileEntities.BANNER, NewBannerTileEntityRenderer::new);

        ScreenManager.registerFactory(BannerAdditionsRegistry.Containers.LOOM, NewLoomScreen::new);
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        BannerAdditionsRegistry.Blocks.registerAllBlocks(event);
    }

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        BannerAdditionsRegistry.TileEntities.registerAllTileEntities(event);
    }

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        BannerAdditionsRegistry.Containers.registerAllContainers(event);
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        BannerAdditionsRegistry.Items.registerAllItems(event);
    }

    @SubscribeEvent
    public static void registerCraftingRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        BannerAdditionsRegistry.CraftingRecipes.registerAllRecipes(event);
    }

    @SubscribeEvent
    public static void atlasTextures(final TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(new ResourceLocation("minecraft:textures/atlas/banner_patterns.png"))) {
            for (int i = 0; i < BannerPatterns.getLength(); i++) {
                event.addSprite(BannerPatterns.get(i).getBannerTexture());
            }
        } else if (event.getMap().getTextureLocation().equals(new ResourceLocation("minecraft:textures/atlas/shield_patterns.png"))) {
            for (int i = 0; i < BannerPatterns.getLength(); i++) {
                event.addSprite(BannerPatterns.get(i).getShieldTexture());
            }
        }
    }
}
