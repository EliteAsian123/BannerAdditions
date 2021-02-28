package eliteasian.mods.banneradditions;

import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternTextureHandler;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntityRenderer;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import eliteasian.mods.banneradditions.loom.NewLoomScreen;
import eliteasian.mods.banneradditions.network.BannerPatternsMessage;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;

@Mod(BannerAdditions.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BannerAdditions {
    public static BannerAdditions INSTANCE;

    public static SimpleChannel SIMPLE_CHANNEL_INSTANCE;

    public static final String MOD_ID = "banneradditions";

    public static final Logger LOGGER = LogManager.getLogger();

    // Compat for morewaterlogging
    public static boolean morewaterloggingLoaded;

    public BannerAdditions() {
        INSTANCE = this;

        FMLJavaModLoadingContext modLoader = FMLJavaModLoadingContext.get();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BannerAdditionsConfig.FORGE_SPEC, "banneradditions.toml");

        modLoader.getModEventBus().addListener(this::setup);
        modLoader.getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);

        MinecraftForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            clientConstructor();
        }

        morewaterloggingLoaded = ModList.get().isLoaded("morewaterlogging");
    }

    @OnlyIn(Dist.CLIENT)
    private void clientConstructor() {
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(BannerPatternTextureHandler.INSTANCE);
    }

    private void setup(final FMLCommonSetupEvent event) {
        SIMPLE_CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "network"), () -> "1", i -> true, i -> true);

        SIMPLE_CHANNEL_INSTANCE.registerMessage(0, BannerPatternsMessage.class, BannerPatternsMessage::encode, BannerPatternsMessage::decode, BannerPatternsMessage::handle);

        BannerPatterns.initStaticBannerPatterns();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(BannerAdditionsRegistry.TileEntities.BANNER, NewBannerTileEntityRenderer::new);

        ScreenManager.registerFactory(BannerAdditionsRegistry.Containers.LOOM, NewLoomScreen::new);
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BannerPatterns());
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

    public static void addBannerInformation(ItemStack stack, List<ITextComponent> tooltip) {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null && compoundnbt.contains("Patterns")) {
            ListNBT listnbt = compoundnbt.getList("Patterns", 10);

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt1 = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(compoundnbt1.getInt("Color"));
                BannerPatternHolder bannerpattern = BannerPatterns.get(compoundnbt1.getString("Pattern"));
                if (bannerpattern != null) {
                    tooltip.add((new TranslationTextComponent(bannerpattern.getTranslationKey() + '.' + dyecolor.getTranslationKey())).mergeStyle(TextFormatting.GRAY));
                }
            }
        }
    }
}
