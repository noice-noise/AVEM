package avem.basic;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Arrays;
import java.util.List;

public class AVEMTheme {

    public static final String VERSION = "AVEM 1.0";

    public static int CURRENT_MODE = 0;
    public static int CURRENT_COLOR = 0;

    public static final int LIGHT_MODE_VALUE = 0;
    public static final int DARK_MODE_VALUE = 1;

    public static final int MAROON_GOLD = 0;
    public static final int FLAME = 1;
    public static final int ICE = 2;
    public static final int LIME = 3;
    public static final int MIDNIGHT = 4;


    private static String BASE = "avem/css/base.css";
    private static String MODE = "avem/css/avem-light-mode.css";
    private static String COLOR = "avem/css/maroon-gold.css";

    private static final String LIGHT_MODE = "avem/css/avem-light-mode.css";
    private static final String DARK_MODE = "avem/css/avem-dark-mode.css";

    private static final String MAROON_GOLD_THEME = "avem/css/maroon-gold.css";
    private static final String FLAME_THEME = "avem/css/flame.css";
    private static final String ICE_THEME = "avem/css/ice.css";
    private static final String LIME_THEME = "avem/css/lime.css";
    private static final String MIDNIGHT_THEME = "avem/css/midnight.css";

    private static final List mode =
            Arrays.asList(
                    LIGHT_MODE,
                    DARK_MODE);

    private static final List themes =
            Arrays.asList(
                    MAROON_GOLD_THEME,
                    FLAME_THEME,
                    ICE_THEME,
                    LIME_THEME,
                    MIDNIGHT_THEME);

    private static final List modeLabels =
            Arrays.asList(
                    "Light",
                    "Dark");

    private static final List themeLabels =
            Arrays.asList(
                    "Maroon&Gold",
                    "Ponkan",
                    "Lemon",
                    "Lime",
                    "Lavender");

    private static Color DARK_GRAY = Color.web("#414141");
    private static Color THEME_IMAGE_ACCENT_COLOR = DARK_GRAY;

    public static final Font FONT_ALERT_HEADERS = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 14);

    private AVEMTheme() { }

    private static void adjustImageColors() {
        if (MODE.equals(DARK_MODE)) {
            THEME_IMAGE_ACCENT_COLOR = Color.WHITE;
        } else {
            THEME_IMAGE_ACCENT_COLOR = DARK_GRAY;
        }
    }

    public static void adjustImgThemeColor(List<ImageView> imagesToAdjust) {
        Lighting lighting = new Lighting(new Light.Distant(45, 90, THEME_IMAGE_ACCENT_COLOR));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);

        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);

        imagesToAdjust.forEach(image -> image.setEffect(lighting));
    }

    public static void setTheme (int mode, int theme) {
        try {
            MODE = getModes().get(mode);
            CURRENT_MODE = mode;
            adjustImageColors();
            COLOR = getThemes().get(theme);
            CURRENT_COLOR = theme;
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<String> getStyleSheets() {
        return Arrays.asList(BASE, COLOR, MODE);
    }

    public static List<String> getThemes() {
        return themes;
    }

    public static List<String> getModes() {
        return mode;
    }

    public static List getModeLabels() {
        return modeLabels;
    }

    public static List getThemeLabels() {
        return themeLabels;
    }

    public static int getCurrentThemeMode() {
        return CURRENT_MODE;
    }

    public static int getCurrentThemeColor() {
        return CURRENT_COLOR;
    }
}
