package pn.nutrimeter.service.factories.user;

public class UserConstants {
    public static final Double DEFAULT_PROTEIN_PERCENTAGE = 0.20;
    public static final Double DEFAULT_CARBOHYDRATE_PERCENTAGE = 0.60;
    public static final Double DEFAULT_LIPID_PERCENTAGE = 0.20;
    public static final Double DEFAULT_KILOS_PER_WEEK = 0.0;

    // MALES
        // PROTEIN
            // AGE 9 - 13
    public static final Double DEFAULT_PROTEIN_RDA_FOR_MALES_AGE_9_13 = 34.0;
            // AGE 14 - 18
    public static final Double DEFAULT_PROTEIN_RDA_FOR_MALES_AGE_14_18 = 52.0;
            // AGE 19 +
    public static final Double DEFAULT_PROTEIN_RDA_FOR_MALES_AGE_19_UP = 56.0;
        // FIBER
            // AGE 9 - 13
    public static final Double DEFAULT_FIBER_RDA_FOR_MALES_AGE_9_13 = 31.0;
            // AGE 14 - 50
    public static final Double DEFAULT_FIBER_RDA_FOR_MALES_AGE_14_50 = 38.0;
            // AGE 51+
    public static final Double DEFAULT_FIBER_RDA_FOR_MALES_AGE_51_UP = 30.0;



    // FEMALES
        // PROTEIN
            // AGE 9 - 13
    public static final Double DEFAULT_PROTEIN_RDA_FOR_FEMALES_AGE_9_13 = 34.0;
            // AGE 14 +
    public static final Double DEFAULT_PROTEIN_RDA_FOR_FEMALES_AGE_14_UP = 46.0;
            // PREGNANCY AND BREASTFEEDING
    public static final Double DEFAULT_PROTEIN_RDA_FOR_FEMALES_PREGNANT_OR_BREASTFEEDING = 71.0;
        // FIBER
            // AGE 9 - 18
    public static final Double DEFAULT_FIBER_RDA_FOR_FEMALES_AGE_9_18 = 26.0;
            // AGE 19 - 50
    public static final Double DEFAULT_FIBER_RDA_FOR_FEMALES_AGE_19_50 = 25.0;
            // AGE 51+
    public static final Double DEFAULT_FIBER_RDA_FOR_FEMALES_AGE_51_UP = 21.0;



    // MALES AND FEMALES
        // CARBS
            // AGE 1+
    public static final Double DEFAULT_CARB_RDA_FOR_MALES_AND_FEMALES_AGE_1_UP = 130.0;
            // PREGNANCY
    public static final Double DEFAULT_CARB_RDA_FOR_FEMALES_PREGNANT = 175.0;
            // BREASTFEEDING
    public static final Double DEFAULT_CARB_RDA_FOR_FEMALES_BREASTFEEDING = 210.0;
        // FATS
    public static final Double DEFAULT_FAT_RDA_FOR_MALES_AND_FEMALES_AGE_1_UP = 65.0;
}
