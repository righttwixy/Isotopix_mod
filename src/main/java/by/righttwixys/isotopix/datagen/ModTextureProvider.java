package by.righttwixys.isotopix.datagen;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.api.ItemRegistryApi;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModTextureProvider implements DataProvider {
    private final PackOutput packOutput;
    private static final Map<String, Integer> ATOMIC_NUMBERS = new HashMap<>();

    static {
        ATOMIC_NUMBERS.put("H", 1);
        ATOMIC_NUMBERS.put("Be", 4);
        ATOMIC_NUMBERS.put("C", 6);
        ATOMIC_NUMBERS.put("Na", 11);
        ATOMIC_NUMBERS.put("P", 15);
        ATOMIC_NUMBERS.put("Cl", 17);
        ATOMIC_NUMBERS.put("K", 19);
        ATOMIC_NUMBERS.put("Ca", 20);
        ATOMIC_NUMBERS.put("Sc", 21);
        ATOMIC_NUMBERS.put("Cr", 24);
        ATOMIC_NUMBERS.put("Mn", 25);
        ATOMIC_NUMBERS.put("Fe", 26);
        ATOMIC_NUMBERS.put("Co", 27);
        ATOMIC_NUMBERS.put("Ni", 28);
        ATOMIC_NUMBERS.put("Zn", 30);
        ATOMIC_NUMBERS.put("Ga", 31);
        ATOMIC_NUMBERS.put("Ge", 32);
        ATOMIC_NUMBERS.put("Se", 34);
        ATOMIC_NUMBERS.put("Kr", 36);
        ATOMIC_NUMBERS.put("Sr", 38);
        ATOMIC_NUMBERS.put("Y", 39);
        ATOMIC_NUMBERS.put("Zr", 40);
        ATOMIC_NUMBERS.put("Nb", 41);
        ATOMIC_NUMBERS.put("Mo", 42);
        ATOMIC_NUMBERS.put("Tc", 43);
        ATOMIC_NUMBERS.put("Ru", 44);
        ATOMIC_NUMBERS.put("Pd", 46);
        ATOMIC_NUMBERS.put("Ag", 47);
        ATOMIC_NUMBERS.put("Cd", 48);
        ATOMIC_NUMBERS.put("In", 49);
        ATOMIC_NUMBERS.put("Sn", 50);
        ATOMIC_NUMBERS.put("Sb", 51);
        ATOMIC_NUMBERS.put("Te", 52);
        ATOMIC_NUMBERS.put("I", 53);
        ATOMIC_NUMBERS.put("Xe", 54);
        ATOMIC_NUMBERS.put("Cs", 55);
        ATOMIC_NUMBERS.put("Ba", 56);
        ATOMIC_NUMBERS.put("La", 57);
        ATOMIC_NUMBERS.put("Ce", 58);
        ATOMIC_NUMBERS.put("Pr", 59);
        ATOMIC_NUMBERS.put("Nd", 60);
        ATOMIC_NUMBERS.put("Pm", 61);
        ATOMIC_NUMBERS.put("Sm", 62);
        ATOMIC_NUMBERS.put("Eu", 63);
        ATOMIC_NUMBERS.put("Gd", 64);
        ATOMIC_NUMBERS.put("Tb", 65);
        ATOMIC_NUMBERS.put("Lu", 71);
        ATOMIC_NUMBERS.put("Ta", 73);
        ATOMIC_NUMBERS.put("W", 74);
        ATOMIC_NUMBERS.put("Re", 75);
        ATOMIC_NUMBERS.put("Ir", 77);
        ATOMIC_NUMBERS.put("Au", 79);
        ATOMIC_NUMBERS.put("Tl", 81);
        ATOMIC_NUMBERS.put("Pb", 82);
        ATOMIC_NUMBERS.put("Bi", 83);
        ATOMIC_NUMBERS.put("Po", 84);
        ATOMIC_NUMBERS.put("Rn", 86);
        ATOMIC_NUMBERS.put("Fr", 87);
        ATOMIC_NUMBERS.put("Ra", 88);
        ATOMIC_NUMBERS.put("Ac", 89);
        ATOMIC_NUMBERS.put("Th", 90);
        ATOMIC_NUMBERS.put("Pa", 91);
        ATOMIC_NUMBERS.put("U", 92);
        ATOMIC_NUMBERS.put("Np", 93);
        ATOMIC_NUMBERS.put("Pu", 94);
        ATOMIC_NUMBERS.put("Am", 95);
        ATOMIC_NUMBERS.put("Cm", 96);
        ATOMIC_NUMBERS.put("Bk", 97);
        ATOMIC_NUMBERS.put("Cf", 98);
        ATOMIC_NUMBERS.put("Es", 99);
        ATOMIC_NUMBERS.put("Fm", 100);
    }

    public ModTextureProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return CompletableFuture.runAsync(() -> {
            Path mainResourcesDir = Path.of("src/main/resources/assets/" + Isotopix.MODID + "/textures/item");
            Path generatedDir = this.packOutput.getOutputFolder().resolve("assets/" + Isotopix.MODID + "/textures/item");

            try {
                Files.createDirectories(mainResourcesDir);
                Files.createDirectories(generatedDir);

                int count = 0;
                for (DeferredItem<Item> itemHolder : ItemRegistryApi.getAllItems()) {
                    String name = itemHolder.getId().getPath();
                    BufferedImage image = createHighQualityTile(name);

                    File mainTarget = mainResourcesDir.resolve(name + ".png").toFile();
                    ImageIO.write(image, "PNG", mainTarget);

                    File genTarget = generatedDir.resolve(name + ".png").toFile();
                    ImageIO.write(image, "PNG", genTarget);

                    count++;
                }

                System.out.println("=================================================");
                System.out.println("[Isotopix] УСПЕШНО СГЕНЕРИРОВАНО ИКОНОК 256x256: " + count);
                System.out.println("[Isotopix] Папка текстур: " + mainResourcesDir.toFile().getAbsolutePath());
                System.out.println("=================================================");
            } catch (IOException e) {
                throw new RuntimeException("Не удалось сохранить сгенерированные текстуры", e);
            }
        });
    }

    private BufferedImage createHighQualityTile(String rawName) {
        final int size = 256;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Color cardBg = new Color(16, 20, 26);
        Color borderAccent = new Color(52, 211, 153);
        String topBadge = "";
        String rightBadge = "";
        String mainSymbol = "U";

        if (rawName.matches("^[a-zA-Z]+[0-9]+[a-zA-Z]*$")) {
            String letters = rawName.replaceAll("[0-9].*$", "");
            String mass = rawName.substring(letters.length());
            mainSymbol = letters.substring(0, 1).toUpperCase() + letters.substring(1).toLowerCase();

            Integer atomic = ATOMIC_NUMBERS.get(mainSymbol);
            topBadge = (atomic != null) ? String.valueOf(atomic) : "Z";
            rightBadge = mass;
            borderAccent = new Color(52, 211, 153);
        } else if (rawName.startsWith("ore_")) {
            cardBg = new Color(24, 18, 14);
            borderAccent = new Color(245, 158, 11);
            topBadge = "ORE";
            String sub = rawName.substring(4);
            rightBadge = getOreFormulaBadge(sub);
            mainSymbol = getOreShortName(sub);
        } else if (rawName.startsWith("mineral_")) {
            cardBg = new Color(14, 24, 18);
            borderAccent = new Color(34, 197, 94);
            topBadge = "MINERAL";
            String sub = rawName.substring(8);
            rightBadge = getMineralFormulaBadge(sub);
            mainSymbol = getMineralShortName(sub);
        } else if (rawName.contains("carbide") || rawName.contains("nitride")) {
            cardBg = new Color(22, 16, 28);
            borderAccent = new Color(168, 85, 247);
            topBadge = "CERAMIC";
            rightBadge = getCompoundFormulaBadge(rawName);
            mainSymbol = getCompoundSymbol(rawName);
        } else if (rawName.contains("oxide") || rawName.endsWith("_peroxide")) {
            cardBg = new Color(28, 16, 16);
            borderAccent = new Color(249, 115, 22);
            topBadge = "OXIDE";
            rightBadge = getCompoundFormulaBadge(rawName);
            mainSymbol = getCompoundSymbol(rawName);
        } else if (rawName.contains("hydroxide") || rawName.contains("diuranate")) {
            cardBg = new Color(26, 24, 14);
            borderAccent = new Color(234, 179, 8);
            topBadge = "HYDR";
            rightBadge = getCompoundFormulaBadge(rawName);
            mainSymbol = getCompoundSymbol(rawName);
        } else {
            cardBg = new Color(14, 22, 28);
            borderAccent = new Color(56, 189, 248);
            topBadge = "SALT";
            rightBadge = getCompoundFormulaBadge(rawName);
            mainSymbol = getCompoundSymbol(rawName);
        }


        g.setColor(cardBg);
        g.fillRect(6, 6, size - 12, size - 12);


        g.setColor(new Color(6, 8, 12));
        g.setStroke(new BasicStroke(4.0f));
        g.drawRect(2, 2, size - 5, size - 5);


        g.setColor(borderAccent);
        g.setStroke(new BasicStroke(4.0f));
        g.drawRect(6, 6, size - 13, size - 13);

        int headerDividerY = 66;
        g.setColor(new Color(borderAccent.getRed(), borderAccent.getGreen(), borderAccent.getBlue(), 160));
        g.setStroke(new BasicStroke(3.0f));
        g.drawLine(12, headerDividerY, size - 13, headerDividerY);


        Font fontBadge = resolveFont(Font.BOLD, 26);
        g.setFont(fontBadge);
        FontMetrics fmBadge = g.getFontMetrics(fontBadge);

        int pillH = 38;
        int pillY = 16;

        int leftPillW = fmBadge.stringWidth(topBadge) + 20;
        int leftPillX = 14;

        g.setColor(new Color(borderAccent.getRed(), borderAccent.getGreen(), borderAccent.getBlue(), 45));
        g.fillRoundRect(leftPillX, pillY, leftPillW, pillH, 8, 8);

        g.setColor(borderAccent);
        g.setStroke(new BasicStroke(2.5f));
        g.drawRoundRect(leftPillX, pillY, leftPillW, pillH, 8, 8);


        g.setColor(new Color(0, 0, 0, 220));
        int textY = pillY + (pillH - fmBadge.getHeight()) / 2 + fmBadge.getAscent();
        g.drawString(topBadge, leftPillX + 11, textY + 1);

        g.setColor(Color.WHITE);
        g.drawString(topBadge, leftPillX + 10, textY);


        int rightPillW = fmBadge.stringWidth(rightBadge) + 20;
        int rightPillX = size - rightPillW - 14;

        g.setColor(new Color(borderAccent.getRed(), borderAccent.getGreen(), borderAccent.getBlue(), 45));
        g.fillRoundRect(rightPillX, pillY, rightPillW, pillH, 8, 8);

        g.setColor(borderAccent);
        g.setStroke(new BasicStroke(2.5f));
        g.drawRoundRect(rightPillX, pillY, rightPillW, pillH, 8, 8);

        g.setColor(new Color(0, 0, 0, 220));
        g.drawString(rightBadge, rightPillX + 11, textY + 1);

        g.setColor(borderAccent);
        g.drawString(rightBadge, rightPillX + 10, textY);


        int symLen = mainSymbol.length();
        int baseFontSize;
        if (symLen <= 2) {
            baseFontSize = 104;
        } else if (symLen <= 4) {
            baseFontSize = 78;
        } else if (symLen <= 6) {
            baseFontSize = 56;
        } else {
            baseFontSize = 44;
        }

        Font fontSymbol = resolveFont(Font.BOLD, baseFontSize);
        g.setFont(fontSymbol);
        FontMetrics fmSym = g.getFontMetrics(fontSymbol);


        while (fmSym.stringWidth(mainSymbol) > (size - 36) && baseFontSize > 22) {
            baseFontSize -= 4;
            fontSymbol = resolveFont(Font.BOLD, baseFontSize);
            g.setFont(fontSymbol);
            fmSym = g.getFontMetrics(fontSymbol);
        }

        int xSym = (size - fmSym.stringWidth(mainSymbol)) / 2;
        int bodyTop = headerDividerY + 6;
        int bodyBottom = size - 14;
        int bodyHeight = bodyBottom - bodyTop;
        int ySym = bodyTop + (bodyHeight - fmSym.getHeight()) / 2 + fmSym.getAscent() + 2;

        g.setColor(new Color(0, 0, 0, 240));
        g.drawString(mainSymbol, xSym + 3, ySym + 3);


        g.setColor(Color.WHITE);
        g.drawString(mainSymbol, xSym, ySym);

        g.dispose();
        return img;
    }

    private static String getOreShortName(String sub) {
        return switch (sub) {
            case "uraninite" -> "UO₂";
            case "pitchblende" -> "U₃O₈";
            case "coffinite" -> "USi";
            case "brannerite" -> "UTi";
            case "davidite" -> "Dav";
            case "samarskite" -> "Sam";
            case "euxenite" -> "Eux";
            case "betafite" -> "Bet";
            case "fergusonite" -> "Fer";
            case "thorite" -> "ThSi";
            case "thorianite" -> "ThO₂";
            case "monazite" -> "Mon";
            case "xenotime" -> "Xen";
            case "allanite" -> "All";
            case "loparite" -> "Lop";
            case "uranothorite" -> "U-Th";
            case "ningyoite" -> "Nin";
            default -> "ORE";
        };
    }

    private static String getOreFormulaBadge(String sub) {
        return switch (sub) {
            case "thorite", "thorianite", "monazite", "allanite", "loparite" -> "Th";
            case "uranothorite", "samarskite", "euxenite", "fergusonite" -> "U+Th";
            default -> "U";
        };
    }

    private static String getMineralShortName(String sub) {
        return switch (sub) {
            case "carnotite" -> "Car";
            case "tyuyamunite" -> "Tyu";
            case "autunite" -> "Aut";
            case "torbernite" -> "Torb";
            case "uranophane" -> "Uph";
            case "saleeite" -> "Sal";
            case "zeunerite" -> "Zeu";
            case "curite" -> "Cur";
            case "becquerelite" -> "Bec";
            case "sklodowskite" -> "Skl";
            case "cuprosklodowskite" -> "Cu-S";
            case "kasolite" -> "Kas";
            case "parsonsite" -> "Par";
            case "boltwoodite" -> "Bol";
            case "liebigite" -> "Lie";
            case "schrockingerite" -> "Sch";
            case "andersonite" -> "And";
            case "billietite" -> "Bil";
            case "francevillite" -> "Fra";
            case "uranosphaerite" -> "Usph";
            default -> "MIN";
        };
    }

    private static String getMineralFormulaBadge(String sub) {
        return switch (sub) {
            case "carnotite" -> "K-V";
            case "tyuyamunite", "autunite", "uranophane", "becquerelite", "liebigite" -> "Ca";
            case "torbernite", "zeunerite", "cuprosklodowskite" -> "Cu";
            case "saleeite", "sklodowskite" -> "Mg";
            case "curite", "kasolite", "parsonsite" -> "Pb";
            case "billietite", "francevillite" -> "Ba";
            case "uranosphaerite" -> "Bi";
            case "andersonite" -> "Na-Ca";
            default -> "U";
        };
    }

    private static String getCompoundSymbol(String name) {
        return switch (name) {
            case "u_dioxide" -> "UO₂";
            case "u_trioxide" -> "UO₃";
            case "u_octaoxide" -> "U₃O₈";
            case "uranyl_peroxide" -> "UO₄";
            case "th_dioxide" -> "ThO₂";
            case "pu_dioxide" -> "PuO₂";
            case "pu_sesquioxide" -> "Pu₂O₃";
            case "np_dioxide" -> "NpO₂";
            case "am_dioxide" -> "AmO₂";
            case "cm_sesquioxide" -> "Cm₂O₃";
            case "po_dioxide" -> "PoO₂";
            case "ra_oxide" -> "RaO";
            case "uranyl_hydroxide" -> "UO₂(OH)₂";
            case "th_hydroxide" -> "Th(OH)₄";
            case "pu_hydroxide" -> "Pu(OH)₄";
            case "am_hydroxide" -> "Am(OH)₃";
            case "ra_hydroxide" -> "Ra(OH)₂";
            case "ammonium_diuranate" -> "ADU";
            case "sodium_diuranate" -> "SDU";
            case "magnesium_diuranate" -> "MDU";
            case "u_hexafluoride" -> "UF₆";
            case "u_tetrafluoride" -> "UF₄";
            case "u_tetrachloride" -> "UCl₄";
            case "u_hexachloride" -> "UCl₆";
            case "u_tetrabromide" -> "UBr₄";
            case "u_tetraiodide" -> "UI₄";
            case "uranyl_nitrate" -> "UNH";
            case "uranyl_sulfate" -> "UO₂SO₄";
            case "uranyl_acetate" -> "UO₂Ac₂";
            case "uranyl_carbonate" -> "UO₂CO₃";
            case "u_monocarbide" -> "UC";
            case "u_dicarbide" -> "UC₂";
            case "u_mononitride" -> "UN";
            case "th_tetrafluoride" -> "ThF₄";
            case "th_tetrachloride" -> "ThCl₄";
            case "th_nitrate" -> "Th(NO₃)₄";
            case "th_dicarbide" -> "ThC₂";
            case "pu_trifluoride" -> "PuF₃";
            case "pu_tetrafluoride" -> "PuF₄";
            case "pu_hexafluoride" -> "PuF₆";
            case "pu_trichloride" -> "PuCl₃";
            case "pu_nitrate" -> "Pu(NO₃)₄";
            case "pu_monocarbide" -> "PuC";
            case "pu_mononitride" -> "PuN";
            case "ra_chloride" -> "RaCl₂";
            case "ra_bromide" -> "RaBr₂";
            case "ra_sulfate" -> "RaSO₄";
            case "ra_carbonate" -> "RaCO₃";
            case "cs137_chloride" -> "¹³⁷CsCl";
            case "sr90_chloride" -> "⁹⁰SrCl₂";
            case "sr90_titanate" -> "⁹⁰SrTiO₃";
            case "co60_pellet" -> "⁶⁰Co";
            default -> "CHEM";
        };
    }

    private static String getCompoundFormulaBadge(String name) {
        if (name.contains("carbide")) return "C";
        if (name.contains("nitride")) return "N";
        if (name.startsWith("u_") || name.startsWith("uranyl_") || name.contains("diuranate")) return "U";
        if (name.startsWith("th_")) return "Th";
        if (name.startsWith("pu_")) return "Pu";
        if (name.startsWith("np_")) return "Np";
        if (name.startsWith("am_")) return "Am";
        if (name.startsWith("cm_")) return "Cm";
        if (name.startsWith("po_")) return "Po";
        if (name.startsWith("ra_")) return "Ra";
        if (name.startsWith("cs137_")) return "¹³⁷Cs";
        if (name.startsWith("sr90_")) return "⁹⁰Sr";
        if (name.startsWith("co60_")) return "⁶⁰Co";
        return "RAD";
    }

    private static Font resolveFont(int style, int size) {
        String[] preferredFonts = {"Segoe UI", "Arial Black", "Trebuchet MS", "Arial", Font.SANS_SERIF};
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFamilies = ge.getAvailableFontFamilyNames();

        for (String preferred : preferredFonts) {
            boolean available = Arrays.stream(availableFamilies)
                    .anyMatch(family -> family.equalsIgnoreCase(preferred));
            if (available) {
                return new Font(preferred, style, size);
            }
        }
        return new Font(Font.SANS_SERIF, style, size);
    }

    @Override
    public String getName() {
        return "Isotopix High-Quality Element Tile Textures";
    }
}