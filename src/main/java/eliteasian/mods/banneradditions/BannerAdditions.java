package eliteasian.mods.banneradditions;

import eliteasian.mods.banneradditions.banner.NewBannerItem;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternTextureHandler;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntityRenderer;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import eliteasian.mods.banneradditions.loom.NewLoomScreen;
import eliteasian.mods.banneradditions.network.BannerPatternsMessage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;

@Mod(BannerAdditions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BannerAdditions {
    public static BannerAdditions INSTANCE;

    public static SimpleChannel SIMPLE_CHANNEL_INSTANCE;

    public static final String MOD_ID = "banneradditions";

    public static final Logger LOGGER = LogManager.getLogger();

    public BannerAdditions() {
        INSTANCE = this;

        FMLJavaModLoadingContext modLoader = FMLJavaModLoadingContext.get();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BannerAdditionsConfig.FORGE_SPEC, "banneradditions.toml");

        modLoader.getModEventBus().addListener(this::setup);
        modLoader.getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);

        MinecraftForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            clientConstructor();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void clientConstructor() {
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(BannerPatternTextureHandler.INSTANCE);
    }

    private void setup(final FMLCommonSetupEvent event) {
        SIMPLE_CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "network"), () -> "1", i -> true, i -> true);

        SIMPLE_CHANNEL_INSTANCE.registerMessage(0, BannerPatternsMessage.class, BannerPatternsMessage::encode, BannerPatternsMessage::decode, BannerPatternsMessage::handle);

        // Villager point of interests
        try {
            Field f = PointOfInterestType.class.getDeclaredField(ASMAPI.mapField("field_221073_u"));
            f.setAccessible(true);

            BannerAdditionsRegistry.Blocks.LOOM.getStateContainer().getValidStates().forEach(s -> putBlockInPOI(f, s, PointOfInterestType.SHEPHERD));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        BannerPatterns.initStaticBannerPatterns();
    }

    private static void putBlockInPOI(Field f, BlockState s, PointOfInterestType p) {
        try {
            ((Map<BlockState, PointOfInterestType>) f.get(null)).put(s, p);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(BannerAdditionsRegistry.TileEntities.BANNER, NewBannerTileEntityRenderer::new);

        ScreenManager.registerFactory(BannerAdditionsRegistry.Containers.LOOM, NewLoomScreen::new);
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        IReloadableResourceManager resourceManager = event.getServer().getResourceManager();

        resourceManager.addReloadListener(new BannerPatterns());
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
    @OnlyIn(Dist.CLIENT)
    public static void onTextureStitch(final TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(new ResourceLocation("minecraft:textures/atlas/banner_patterns.png"))) {
            for (ResourceLocation i : BannerPatternTextureHandler.bannerTextures) {
                event.addSprite(i);
            }
        } else if (event.getMap().getTextureLocation().equals(new ResourceLocation("minecraft:textures/atlas/shield_patterns.png"))) {
            for (ResourceLocation i : BannerPatternTextureHandler.shieldTextures) {
                event.addSprite(i);
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerInteract(@Nonnull PlayerInteractEvent.RightClickBlock event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        if (block == Blocks.CAULDRON) {
            ItemStack itemstack = event.getItemStack();

            if (itemstack.getItem() instanceof NewBannerItem) {
                NewBannerItem.handleCauldron(event.getPlayer(), itemstack, event.getWorld(), event.getPos(), event.getHand());

                event.setResult(Event.Result.ALLOW);
                event.setCanceled(true);
            }
        }
    }
}
