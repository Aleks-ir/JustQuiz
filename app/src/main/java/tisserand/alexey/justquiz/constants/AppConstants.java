package tisserand.alexey.justquiz.constants;

public class AppConstants {
    public static final String APP_PREF_NAME = "quiz_app_pref";
    public static final String KEY_SOUND = "sound";
    public static final String KEY_VIBRO = "vibro";
    public static final String KEY_BACKGROUND = "background";
    public static final String KEY_SQ = "sq";
    public static final String KEY_SA = "sa";

    public static final int BUNDLE_KEY_DEFAULT_SQ = 25;
    public static final int BUNDLE_KEY_DEFAULT_SA = 20;

    public static final int BUNDLE_KEY_ZERO_INDEX = 0;
    public static final int BUNDLE_KEY_FIRST_INDEX = 1;
    public static final int BUNDLE_KEY_SECOND_INDEX = 2;
    public static final int BUNDLE_KEY_THIRD_INDEX = 3;
    public static final long BUNDLE_KEY_VIBRATOR = 300L;
    public static final int BUNDLE_KEY_TIME = 60;



    public static final String COLOR_WHITE = "rectangle_white_normal";
    public static final String COLOR_GREEN = "rectangle_green_normal";
    public static final String COLOR_RED = "rectangle_red_normal";

    public static final String EMPTY_STRING = "";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_MESSAGE = "message";

    public static final String BUNDLE_KEY_ALL = "all";
    public static final String BUNDLE_KEY_COMMON = "common";

    // notification constants
    public static final String PREF_NOTIFICATION = "pref_notification";
    public static final String PREF_FONT_SIZE = "pref_font_size";
    public static final String BUNDLE_KEY_ITEM = "item";
    public static final String BUNDLE_KEY_INDEX = "index";
    public static final String BUNDLE_KEY_BOOL = "bool";
    public static final String PREF_SCORE = "score";

    // question constants
    public static final String BUNDLE_KEY_YES = "yes";
    public static final String BUNDLE_KEY_NO = "no";
    public static final String BUNDLE_KEY_VIEW_ID = "view_id_tex";
    public static final String BUNDLE_KEY_DIALOG_FRAGMENT = "dialog_fragment";
    public static final String BUNDLE_KEY_CLOSE_OPTION = "close";
    public static final String BUNDLE_KEY_SKIP_OPTION = "skip";

    // category file
    public static final String CONTENT_FILE = "json/categories/quiz_category.json";
    public static final String JSON_KEY_ITEMS = "items";
    public static final String JSON_KEY_CATEGORY_ID = "question_category";
    public static final String JSON_KEY_CATEGORY_NAME = "category_name";

    // question file
    public static final String QUESTION_FILE = "json/quiz/question_set.json";
    public static final String JSON_KEY_QUESTIONNAIRY = "questionnaires";
    public static final String JSON_KEY_QUESTION = "question";
    public static final String JSON_KEY_CORRECT_ANS = "correct_answer";
    public static final String JSON_KEY_ANSWERS = "answers";
    public static final String QUESTIONS_IN_TEST = "questions_count";
}
