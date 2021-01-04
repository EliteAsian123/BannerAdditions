package eliteasian.mods.banneradditions.bannerpattern;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.BannerAdditionsConfig;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class BannerPatterns implements IResourceManagerReloadListener {
    private static final List<BannerPatternHolder> bannerPatterns = new ArrayList<>();
    private static final List<BannerPatternHolder> staticBannerPatterns = new ArrayList<>();

    private static final Gson GSON = new Gson();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManagerIn) {
        Map<ResourceLocation, JsonElement> map = new HashMap<>();

        for (ResourceLocation resourceLocation : resourceManagerIn.getAllResourceLocations("banner_patterns", i -> i.endsWith(".json"))) {
            try (IResource resource = resourceManagerIn.getResource(resourceLocation)) {
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                ResourceLocation newResourceLocation = new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath().substring(0, resourceLocation.getPath().length() - 5));
                map.put(newResourceLocation, GSON.fromJson(reader, JsonElement.class));
            } catch (Exception e) {
                BannerAdditions.LOGGER.error("Error loading Banner Pattern " + resourceLocation, e);
            }
        }

        apply(map, resourceManagerIn);
    }

    private void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn) {
        clear();

        bannerPatterns.addAll(staticBannerPatterns);

        if (!BannerAdditionsConfig.CONFIG.safeMode.get()) {
            for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
                ResourceLocation resourceLocation = entry.getKey();

                // Skip Metadata
                if(resourceLocation.getPath().startsWith("_"))
                    continue;

                JsonObject json = JSONUtils.getJsonObject(entry.getValue(), "top element");

                String hashname = JSONUtils.getString(json, "hashname");

                ResourceLocation bannerTextureRaw = new ResourceLocation(JSONUtils.getString(json, "bannerTexture"));
                ResourceLocation shieldTextureRaw = new ResourceLocation(JSONUtils.getString(json, "shieldTexture"));

                ResourceLocation bannerTexture = new ResourceLocation(bannerTextureRaw.getNamespace(), "entity/banner/" + bannerTextureRaw.getPath());
                ResourceLocation shieldTexture = new ResourceLocation(shieldTextureRaw.getNamespace(), "entity/shield/" + shieldTextureRaw.getPath());

                String[] pathSplit = resourceLocation.getPath().split("/");
                String name = pathSplit[pathSplit.length - 1];

                if (JSONUtils.hasField(json, "item")) {
                    ResourceLocation item = new ResourceLocation(JSONUtils.getString(json, "item"));

                    add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, name, item));
                } else {
                    add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, name));
                }
            }
        }
    }

    public static void regStatic(String name, String hashname, Item item) {
        staticBannerPatterns.add(new BannerPatternHolder(new ResourceLocation("minecraft", "entity/banner/" + name), new ResourceLocation("minecraft", "entity/shield/" + name), hashname, name, item.getRegistryName()));
    }

    public static void regStatic(String name, String hashname) {
        regStatic(name, hashname, Items.AIR);
    }

    public static void add(BannerPatternHolder bannerPatternHolder) {
        bannerPatterns.add(bannerPatternHolder);
    }

    public static void clear() {
        bannerPatterns.clear();
    }

    public static BannerPatternHolder get(int i) {
        return bannerPatterns.get(i);
    }

    public static BannerPatternHolder get(String hash) {
        for (BannerPatternHolder i : bannerPatterns) {
            if (i.getHashname().equals(hash)) {
                return i;
            }
        }

        return null;
    }

    public static int getLength() {
        return bannerPatterns.size();
    }

    public static ListNBT toListNBT(Pair<BannerPatternHolder, DyeColor>... patterns) {
        ListNBT listnbt = new ListNBT();

        for (Pair<BannerPatternHolder, DyeColor> pair : patterns) {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putString("Pattern", (pair.getFirst()).getHashname());
            compoundnbt.putInt("Color", pair.getSecond().getId());
            listnbt.add(compoundnbt);
        }

        return listnbt;
    }

    public static BannerPatternHolder[] getWithItem(Item item) {
        Item usingItem = item;
        if (usingItem == null)
            usingItem = Items.AIR;

        List<BannerPatternHolder> out = new ArrayList<BannerPatternHolder>();

        for (BannerPatternHolder pattern : bannerPatterns) {
            if (pattern.getItem().equals(usingItem.getRegistryName())) {
                out.add(pattern);
            }
        }

        return out.toArray(new BannerPatternHolder[0]);
    }

    public static List<BannerPatternHolder> getAsList() {
        return new ArrayList<BannerPatternHolder>(bannerPatterns);
    }

    public static void initStaticBannerPatterns() {
        List<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof BannerPatternItem).collect(Collectors.toList());

        for (BannerPattern pattern : BannerPattern.values()) {
            List<Item> patternItem = items.stream().filter(item -> ((BannerPatternItem) item).getBannerPattern().equals(pattern)).collect(Collectors.toList());
            if (patternItem.size() <= 0)
                regStatic(getBannerPatternFileName(pattern), pattern.getHashname());
            else
                regStatic(getBannerPatternFileName(pattern), pattern.getHashname(), patternItem.get(0));
        }
    }

    // We do this because BannerPattern.getFileName() is client only
    private static String getBannerPatternFileName(BannerPattern bannerPattern) {
        try {
            Field field = BannerPattern.class.getDeclaredField(ASMAPI.mapField("field_191014_N"));
            field.setAccessible(true);
            return (String) field.get(bannerPattern);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
