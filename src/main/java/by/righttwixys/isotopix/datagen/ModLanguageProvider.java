package by.righttwixys.isotopix.datagen;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.api.ItemRegistryApi;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.Map;

public class ModLanguageProvider extends LanguageProvider {
    private final boolean isRussian;
    private static final Map<String, String> RU_NAMES = new HashMap<>();
    private static final Map<String, String> EN_NAMES = new HashMap<>();

    static {
        // 1. Руды
        RU_NAMES.put("ore_uraninite", "Уранинит (Смоляная руда)");
        EN_NAMES.put("ore_uraninite", "Uraninite Ore");
        RU_NAMES.put("ore_pitchblende", "Настуран (Урановая смолка)");
        EN_NAMES.put("ore_pitchblende", "Pitchblende");
        RU_NAMES.put("ore_coffinite", "Коффинит");
        EN_NAMES.put("ore_coffinite", "Coffinite");
        RU_NAMES.put("ore_brannerite", "Браннерит");
        EN_NAMES.put("ore_brannerite", "Brannerite");
        RU_NAMES.put("ore_davidite", "Давидит");
        EN_NAMES.put("ore_davidite", "Davidite-(La)");
        RU_NAMES.put("ore_samarskite", "Самарскит");
        EN_NAMES.put("ore_samarskite", "Samarskite-(Y)");
        RU_NAMES.put("ore_euxenite", "Эвксенит");
        EN_NAMES.put("ore_euxenite", "Euxenite-(Y)");
        RU_NAMES.put("ore_betafite", "Бетафит");
        EN_NAMES.put("ore_betafite", "Betafite");
        RU_NAMES.put("ore_fergusonite", "Фергюсонит");
        EN_NAMES.put("ore_fergusonite", "Fergusonite-(Y)");
        RU_NAMES.put("ore_thorite", "Торит");
        EN_NAMES.put("ore_thorite", "Thorite");
        RU_NAMES.put("ore_thorianite", "Торианит");
        EN_NAMES.put("ore_thorianite", "Thorianite");
        RU_NAMES.put("ore_monazite", "Монацит");
        EN_NAMES.put("ore_monazite", "Monazite-(Ce)");
        RU_NAMES.put("ore_xenotime", "Ксенотим");
        EN_NAMES.put("ore_xenotime", "Xenotime-(Y)");
        RU_NAMES.put("ore_allanite", "Алланит (Ортит)");
        EN_NAMES.put("ore_allanite", "Allanite-(Ce)");
        RU_NAMES.put("ore_loparite", "Лопарит");
        EN_NAMES.put("ore_loparite", "Loparite-(Ce)");
        RU_NAMES.put("ore_uranothorite", "Ураноторит");
        EN_NAMES.put("ore_uranothorite", "Uranothorite");
        RU_NAMES.put("ore_ningyoite", "Нингёит");
        EN_NAMES.put("ore_ningyoite", "Ningyoite");

        // 2. Вторичные минералы
        RU_NAMES.put("mineral_carnotite", "Карнотит");
        EN_NAMES.put("mineral_carnotite", "Carnotite");
        RU_NAMES.put("mineral_tyuyamunite", "Тюямунит");
        EN_NAMES.put("mineral_tyuyamunite", "Tyuyamunite");
        RU_NAMES.put("mineral_autunite", "Отенит");
        EN_NAMES.put("mineral_autunite", "Autunite");
        RU_NAMES.put("mineral_torbernite", "Торбернит");
        EN_NAMES.put("mineral_torbernite", "Torbernite");
        RU_NAMES.put("mineral_uranophane", "Уранофан");
        EN_NAMES.put("mineral_uranophane", "Uranophane");
        RU_NAMES.put("mineral_saleeite", "Салеит");
        EN_NAMES.put("mineral_saleeite", "Saleeite");
        RU_NAMES.put("mineral_zeunerite", "Цейнерит");
        EN_NAMES.put("mineral_zeunerite", "Zeunerite");
        RU_NAMES.put("mineral_curite", "Кюрит");
        EN_NAMES.put("mineral_curite", "Curite");
        RU_NAMES.put("mineral_becquerelite", "Беккерелит");
        EN_NAMES.put("mineral_becquerelite", "Becquerelite");
        RU_NAMES.put("mineral_sklodowskite", "Склодовскит");
        EN_NAMES.put("mineral_sklodowskite", "Sklodowskite");
        RU_NAMES.put("mineral_cuprosklodowskite", "Купросклодовскит");
        EN_NAMES.put("mineral_cuprosklodowskite", "Cuprosklodowskite");
        RU_NAMES.put("mineral_kasolite", "Казолит");
        EN_NAMES.put("mineral_kasolite", "Kasolite");
        RU_NAMES.put("mineral_parsonsite", "Парсонсит");
        EN_NAMES.put("mineral_parsonsite", "Parsonsite");
        RU_NAMES.put("mineral_boltwoodite", "Болтвудит");
        EN_NAMES.put("mineral_boltwoodite", "Boltwoodite");
        RU_NAMES.put("mineral_liebigite", "Либигит");
        EN_NAMES.put("mineral_liebigite", "Liebigite");
        RU_NAMES.put("mineral_schrockingerite", "Шрёкингерит");
        EN_NAMES.put("mineral_schrockingerite", "Schrockingerite");
        RU_NAMES.put("mineral_andersonite", "Андерсонит");
        EN_NAMES.put("mineral_andersonite", "Andersonite");
        RU_NAMES.put("mineral_billietite", "Биллиетит");
        EN_NAMES.put("mineral_billietite", "Billietite");
        RU_NAMES.put("mineral_francevillite", "Франсевиллит");
        EN_NAMES.put("mineral_francevillite", "Francevillite");
        RU_NAMES.put("mineral_uranosphaerite", "Ураносферит");
        EN_NAMES.put("mineral_uranosphaerite", "Uranosphaerite");

        // 3. Оксиды, гидроксиды, уранаты
        RU_NAMES.put("u_dioxide", "Диоксид урана (UO2)");
        EN_NAMES.put("u_dioxide", "Uranium Dioxide (UO2)");
        RU_NAMES.put("u_trioxide", "Триоксид урана (UO3)");
        EN_NAMES.put("u_trioxide", "Uranium Trioxide (UO3)");
        RU_NAMES.put("u_octaoxide", "Закись-окись урана (U3O8)");
        EN_NAMES.put("u_octaoxide", "Triuranium Octaoxide (U3O8)");
        RU_NAMES.put("uranyl_peroxide", "Пероксид уранила (UO4)");
        EN_NAMES.put("uranyl_peroxide", "Uranyl Peroxide (UO4)");
        RU_NAMES.put("th_dioxide", "Диоксид тория (ThO2)");
        EN_NAMES.put("th_dioxide", "Thorium Dioxide (ThO2)");
        RU_NAMES.put("pu_dioxide", "Диоксид плутония (PuO2)");
        EN_NAMES.put("pu_dioxide", "Plutonium Dioxide (PuO2)");
        RU_NAMES.put("pu_sesquioxide", "Сесквиоксид плутония (Pu2O3)");
        EN_NAMES.put("pu_sesquioxide", "Plutonium Sesquioxide (Pu2O3)");
        RU_NAMES.put("np_dioxide", "Диоксид нептуния (NpO2)");
        EN_NAMES.put("np_dioxide", "Neptunium Dioxide (NpO2)");
        RU_NAMES.put("am_dioxide", "Диоксид америция (AmO2)");
        EN_NAMES.put("am_dioxide", "Americium Dioxide (AmO2)");
        RU_NAMES.put("cm_sesquioxide", "Сесквиоксид кюрия (Cm2O3)");
        EN_NAMES.put("cm_sesquioxide", "Curium Sesquioxide (Cm2O3)");
        RU_NAMES.put("po_dioxide", "Диоксид полония (PoO2)");
        EN_NAMES.put("po_dioxide", "Polonium Dioxide (PoO2)");
        RU_NAMES.put("ra_oxide", "Оксид радия (RaO)");
        EN_NAMES.put("ra_oxide", "Radium Oxide (RaO)");
        RU_NAMES.put("uranyl_hydroxide", "Гидроксид уранила (UO2(OH)2)");
        EN_NAMES.put("uranyl_hydroxide", "Uranyl Hydroxide (UO2(OH)2)");
        RU_NAMES.put("th_hydroxide", "Гидроксид тория (Th(OH)4)");
        EN_NAMES.put("th_hydroxide", "Thorium Hydroxide (Th(OH)4)");
        RU_NAMES.put("pu_hydroxide", "Гидроксид плутония(IV)");
        EN_NAMES.put("pu_hydroxide", "Plutonium(IV) Hydroxide");
        RU_NAMES.put("am_hydroxide", "Гидроксид америция(III)");
        EN_NAMES.put("am_hydroxide", "Americium(III) Hydroxide");
        RU_NAMES.put("ra_hydroxide", "Гидроксид радия (Ra(OH)2)");
        EN_NAMES.put("ra_hydroxide", "Radium Hydroxide");
        RU_NAMES.put("ammonium_diuranate", "Диуранат аммония (Жёлтый кек / ADU)");
        EN_NAMES.put("ammonium_diuranate", "Ammonium Diuranate (ADU)");
        RU_NAMES.put("sodium_diuranate", "Диуранат натрия (SDU)");
        EN_NAMES.put("sodium_diuranate", "Sodium Diuranate (SDU)");
        RU_NAMES.put("magnesium_diuranate", "Диуранат магния (MDU)");
        EN_NAMES.put("magnesium_diuranate", "Magnesium Diuranate (MDU)");

        // 4. Соли, карбиды, нитриды
        RU_NAMES.put("u_hexafluoride", "Гексафторид урана (UF6)");
        EN_NAMES.put("u_hexafluoride", "Uranium Hexafluoride (UF6)");
        RU_NAMES.put("u_tetrafluoride", "Тетрафторид урана (Зелёная соль / UF4)");
        EN_NAMES.put("u_tetrafluoride", "Uranium Tetrafluoride (UF4)");
        RU_NAMES.put("u_tetrachloride", "Тетрахлорид урана (UCl4)");
        EN_NAMES.put("u_tetrachloride", "Uranium Tetrachloride (UCl4)");
        RU_NAMES.put("u_hexachloride", "Гексахлорид урана (UCl6)");
        EN_NAMES.put("u_hexachloride", "Uranium Hexachloride (UCl6)");
        RU_NAMES.put("u_tetrabromide", "Тетрабромид урана (UBr4)");
        EN_NAMES.put("u_tetrabromide", "Uranium Tetrabromide (UBr4)");
        RU_NAMES.put("u_tetraiodide", "Тетраиодид урана (UI4)");
        EN_NAMES.put("u_tetraiodide", "Uranium Tetraiodide (UI4)");
        RU_NAMES.put("uranyl_nitrate", "Нитрат уранила гексагидрат (UNH)");
        EN_NAMES.put("uranyl_nitrate", "Uranyl Nitrate Hexahydrate");
        RU_NAMES.put("uranyl_sulfate", "Сульфат уранила тригидрат");
        EN_NAMES.put("uranyl_sulfate", "Uranyl Sulfate Trihydrate");
        RU_NAMES.put("uranyl_acetate", "Ацетат уранила дигидрат");
        EN_NAMES.put("uranyl_acetate", "Uranyl Acetate Dihydrate");
        RU_NAMES.put("uranyl_carbonate", "Карбонат уранила (Резерфордин)");
        EN_NAMES.put("uranyl_carbonate", "Uranyl Carbonate (Rutherfordine)");
        RU_NAMES.put("u_monocarbide", "Монокарбид урана (UC)");
        EN_NAMES.put("u_monocarbide", "Uranium Monocarbide (UC)");
        RU_NAMES.put("u_dicarbide", "Дикарбид урана (UC2)");
        EN_NAMES.put("u_dicarbide", "Uranium Dicarbide (UC2)");
        RU_NAMES.put("u_mononitride", "Мононитрид урана (UN)");
        EN_NAMES.put("u_mononitride", "Uranium Mononitride (UN)");
        RU_NAMES.put("th_tetrafluoride", "Тетрафторид тория (ThF4)");
        EN_NAMES.put("th_tetrafluoride", "Thorium Tetrafluoride (ThF4)");
        RU_NAMES.put("th_tetrachloride", "Тетрахлорид тория (ThCl4)");
        EN_NAMES.put("th_tetrachloride", "Thorium Tetrachloride (ThCl4)");
        RU_NAMES.put("th_nitrate", "Нитрат тория тетрагидрат");
        EN_NAMES.put("th_nitrate", "Thorium Nitrate Tetrahydrate");
        RU_NAMES.put("th_dicarbide", "Дикарбид тория (ThC2)");
        EN_NAMES.put("th_dicarbide", "Thorium Dicarbide (ThC2)");
        RU_NAMES.put("pu_trifluoride", "Трифторид плутония (PuF3)");
        EN_NAMES.put("pu_trifluoride", "Plutonium Trifluoride (PuF3)");
        RU_NAMES.put("pu_tetrafluoride", "Тетрафторид плутония (PuF4)");
        EN_NAMES.put("pu_tetrafluoride", "Plutonium Tetrafluoride (PuF4)");
        RU_NAMES.put("pu_hexafluoride", "Гексафторид плутония (PuF6)");
        EN_NAMES.put("pu_hexafluoride", "Plutonium Hexafluoride (PuF6)");
        RU_NAMES.put("pu_trichloride", "Трихлорид плутония (PuCl3)");
        EN_NAMES.put("pu_trichloride", "Plutonium Trichloride (PuCl3)");
        RU_NAMES.put("pu_nitrate", "Нитрат плутония(IV)");
        EN_NAMES.put("pu_nitrate", "Plutonium(IV) Nitrate");
        RU_NAMES.put("pu_monocarbide", "Монокарбид плутония (PuC)");
        EN_NAMES.put("pu_monocarbide", "Plutonium Monocarbide (PuC)");
        RU_NAMES.put("pu_mononitride", "Мононитрид плутония (PuN)");
        EN_NAMES.put("pu_mononitride", "Plutonium Mononitride (PuN)");
        RU_NAMES.put("ra_chloride", "Хлорид радия (RaCl2)");
        EN_NAMES.put("ra_chloride", "Radium Chloride (RaCl2)");
        RU_NAMES.put("ra_bromide", "Бромид радия (RaBr2)");
        EN_NAMES.put("ra_bromide", "Radium Bromide (RaBr2)");
        RU_NAMES.put("ra_sulfate", "Сульфат радия (RaSO4)");
        EN_NAMES.put("ra_sulfate", "Radium Sulfate (RaSO4)");
        RU_NAMES.put("ra_carbonate", "Карбонат радия (RaCO3)");
        EN_NAMES.put("ra_carbonate", "Radium Carbonate (RaCO3)");
        RU_NAMES.put("cs137_chloride", "Хлорид цезия-137 (137CsCl)");
        EN_NAMES.put("cs137_chloride", "Caesium-137 Chloride (137CsCl)");
        RU_NAMES.put("sr90_chloride", "Хлорид стронция-90 (90SrCl2)");
        EN_NAMES.put("sr90_chloride", "Strontium-90 Chloride (90SrCl2)");
        RU_NAMES.put("sr90_titanate", "Титанат стронция-90 (90SrTiO3)");
        EN_NAMES.put("sr90_titanate", "Strontium-90 Titanate (90SrTiO3)");
        RU_NAMES.put("co60_pellet", "Кобальт-60 (Источник / 60Co)");
        EN_NAMES.put("co60_pellet", "Cobalt-60 Industrial Pellet");
    }

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, Isotopix.MODID, locale);
        this.isRussian = "ru_ru".equalsIgnoreCase(locale);
    }

    @Override
    protected void addTranslations() {
        if (isRussian) {
            add("itemGroup.isotopix.equipment", "Isotopix: Оборудование и защита");
            add("itemGroup.isotopix.stable", "Isotopix: Стабильные изотопы");
            add("itemGroup.isotopix.alpha", "Isotopix: Альфа-излучатели (α)");
            add("itemGroup.isotopix.beta", "Isotopix: Бета-излучатели (β)");
            add("itemGroup.isotopix.gamma", "Isotopix: Гамма-излучатели (γ)");
            add("itemGroup.isotopix.fission", "Isotopix: Деление и нейтроны (n / SF)");
            add("itemGroup.isotopix.ores", "Isotopix: Природные руды");
            add("itemGroup.isotopix.minerals", "Isotopix: Вторичные минералы");
            add("itemGroup.isotopix.compounds", "Isotopix: Химические соединения");
        } else {
            add("itemGroup.isotopix.equipment", "Isotopix: Equipment & Protection");
            add("itemGroup.isotopix.stable", "Isotopix: Stable Isotopes");
            add("itemGroup.isotopix.alpha", "Isotopix: Alpha Emitters (α)");
            add("itemGroup.isotopix.beta", "Isotopix: Beta Emitters (β)");
            add("itemGroup.isotopix.gamma", "Isotopix: Gamma Emitters (γ)");
            add("itemGroup.isotopix.fission", "Isotopix: Fission & Neutrons (n / SF)");
            add("itemGroup.isotopix.ores", "Isotopix: Natural Radioactive Ores");
            add("itemGroup.isotopix.minerals", "Isotopix: Secondary Minerals");
            add("itemGroup.isotopix.compounds", "Isotopix: Chemical Compounds");
        }

        Map<String, String> enDescMap = ItemRegistryApi.getEnDescriptions();
        Map<String, String> ruDescMap = ItemRegistryApi.getRuDescriptions();

        for (DeferredItem<Item> itemHolder : ItemRegistryApi.getAllItems()) {
            String name = itemHolder.getId().getPath();

            String displayName;
            if (isRussian) {
                displayName = RU_NAMES.getOrDefault(name, formatIsotopeName(name));
            } else {
                displayName = EN_NAMES.getOrDefault(name, formatIsotopeName(name));
            }

            addItem(itemHolder, displayName);

            String descKey = itemHolder.get().getDescriptionId() + ".desc";
            if (isRussian) {
                String desc = ruDescMap.getOrDefault(name, "Радиоактивный материал " + displayName);
                add(descKey, desc);
            } else {
                String desc = enDescMap.getOrDefault(name, "Radioactive material " + displayName);
                add(descKey, desc);
            }
        }
    }

    private String formatIsotopeName(String raw) {
        if (raw == null || raw.isEmpty()) return "";

        if (raw.matches("^[a-zA-Z]+[0-9]+[a-zA-Z]*$")) {
            String letters = raw.replaceAll("[0-9].*$", "");
            String remainder = raw.substring(letters.length());

            String formattedLetters = letters.substring(0, 1).toUpperCase() + letters.substring(1).toLowerCase();
            return formattedLetters + "-" + remainder;
        }

        String[] parts = raw.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                if (!sb.isEmpty()) sb.append(" ");
                sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }
        return sb.toString();
    }
}