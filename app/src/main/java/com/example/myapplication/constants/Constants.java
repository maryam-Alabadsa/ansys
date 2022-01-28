package com.example.myapplication.constants;

import android.content.SharedPreferences;

import com.example.myapplication.fav.Favorite;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.Library;

import java.util.ArrayList;

public class Constants {
    public static final int FROM_ADAPTER_TO_BOOK_DES = 7;
    ///--------------------EventBus-------------------------------------
    public static final int FROM_USER_TO_CHANGE_PASSWORD = 100,
            FROM_EXPLORE_TO_MOST_LISTENED = 200,
            FROM_EXPLORE_TO_LATEST_PUBLISHED = 300,
            FROM_CHANGE_PASSWORD_TO_USER = 400,
            FROM_BOOK_DES_TO_EXO_PLAYER = 500,
            FROM_FRAGMENT_DES_TO_INTRO = 800,
            FROM_SETTINGS_TO_FAV= 900,
            IMG_MAN1 = 1, IMG_MAN2 = 2, IMG_MAN3 = 3, IMG_WOMAN1 = 4, IMG_WOMAN2 = 5, IMG_WOMAN3 = 6;
    public static final String IMG_KEY = "IMG KEY";
    public static final String INTENT_KEY = "INTENT_KEY";
    public static final int FROM_NEW_BOOKS_TO_BOOK_DES = 600;
    public static final int FROM_MOST_LISTENED_BOOKS_TO_BOOK_DES = 700;
    public static final int FROM_SETTINGS_TO_ABOUT_US = 900000;

    public static Library LIST_LIBRARY = new Library();
    public static ArrayList<ConstantsList> LIST_FAVORITE = new ArrayList<>();
    public static ArrayList<ConstantsList> LIST = new ArrayList<>();
//    public static ArrayList<ConstantsList> LIST_NEW = new ArrayList<>();
//    public static ArrayList<ConstantsList> LIST_MOST_LISTENED = new ArrayList<>();

    public static final String HTML_TEXT = "   <p>نقدر مخاوفكم واهتمامكم بشأن خصوصية بياناتكم على شبكة الإنترنت.</p><br>\n" +
            "    \n" +
            "    <p>لقد تم إعداد هذه السياسة لمساعدتكم في تفهم طبيعة البيانات التي نقوم بتجميعها منكم عند زيارتكم ل (موقع / تطبيق) \n" +
            "        أخضر على شبكة الانترنت وكيفية تعاملنا مع هذه البيانات الشخصية.</p><br>\n" +
            "        <h3>التصفح</h3><br><p>لم نقم بتصميم أخضر من أجل تجميع بياناتك الشخصية أثناء التصفح، وإنما سيتم فقط استخدام البيانات المقدمة من قبلك بمعرفتك ومحض إرادتك.</p>\n" +
            "        <br> <h3>عنوان بروتوكول شبكة الإنترنت (IP)</h3><br><p>في أي وقت تزور فيه اي موقع انترنت بما فيها أخضر، سيقوم السيرفر المضيف بتسجيل عنوان بروتوكول شبكة الإنترنت (IP) الخاص بك، تاريخ ووقت الزيارة ونوع متصفح الإنترنت الذي تستخدمه والعنوان URL الخاص بأي موقع من مواقع الإنترنت التي تقوم بإحالتك إلى أخضر على الشبكة. \n" +
            "            \n" +
            "        </p>\n" +
            "        <br><h3>عمليات المسح على الشبكة </h3><br>\n" +
            "        <p>إن عمليات المسح التي نقوم بها مباشرة على الشبكة تمكننا من تجميع بيانات محددة مثل البيانات المطلوبة منك بخصوص نظرتك وشعورك تجاه موقعنا/ تطبيقنا. تعتبر ردودك ذات أهمية قصوى، ومحل تقديرنا حيث أنها تمكننا من تحسين المستوى، ولك كامل الحرية والإختيار في تقديم البيانات المتعلقة بإسمك والبيانات الأخرى. </p>\n" +
            "        <br><h3>إفشاء المعلومات </h3><br>\n" +
            "        <p>سنحافظ في كافة الأوقات على خصوصية وسرية كافة البيانات الشخصية التي نتحصل عليها. ولن يتم إفشاء هذه المعلومات إلا إذا كان ذلك مطلوباً بموجب أي قانون أو عندما نعتقد بحسن نية أن مثل هذا الإجراء سيكون مطلوباً أو مرغوباً فيه للتمشي مع القانون، أو للدفاع عن أو حماية حقوق الملكية الخاصة بهذا الموقع أو الجهات المستفيدة منه. </p>\n" +
            "\n" +
            "        <br><h3>البيانات اللازمة لتنفيذ المعاملات المطلوبة من قبلك \n" +
            "        </h3><br><p>عندما نحتاج إلى أية بيانات خاصة بك , فإننا سنطلب منك تقديمها بمحض إرادتك. حيث ستساعدنا هذه المعلومات في الاتصال بك وتنفيذ طلباتك حيثما كان ذلك ممكنناً. لن يتم اطلاقاً بيع البيانات المقدمة من قبلك إلى أي طرف ثالث بغرض تسويقها لمصلحته الخاصة دون الحصول على موافقتك المسبقة والمكتوبة ما لم يتم ذلك على أساس أنها ضمن بيانات جماعية تستخدم للأغراض الإحصائية والأبحاث دون اشتمالها على أية بيانات من الممكن استخدامها للتعريف بك.</p>\n" +
            "        <br> <h3>عند الاتصال بنا</h3><br><p>سيتم التعامل مع كافة البيانات المقدمة من قبلك على أساس أنها سرية. تتطلب النماذج التي يتم تقديمها مباشرة على الشبكة تقديم البيانات التي ستساعدنا في تحسين موقعنا. سيتم استخدام البيانات التي يتم تقديمها من قبلك في الرد على كافة استفساراتك، ملاحظاتك، أو طلباتك من قبل هذا التطبيق أو أيا من المواقع التابعة له. </p>\n" +
            "        <br><h3>إفشاء المعلومات لأي طرف ثالث</h3><br><p>لن نقوم ببيع، المتاجرة، تأجير، أو إفشاء أية معلومات لمصلحة أي طرف ثالث خارج أخضر. وسيتم الكشف عن المعلومات فقط في حالة صدور أمر بذلك من قبل أي سلطة قضائية أو تنظيمية. </p>\n" +
            "        <br><h3>التعديلات على سياسة سرية وخصوصية المعلومات</h3><br><p>نحتفظ بالحق في تعديل بنود وشروط سياسة سرية وخصوصية المعلومات إن لزم الأمر ومتى كان ذلك ملائماً. سيتم تنفيذ التعديلات هنا او على صفحة سياسة الخصوصية الرئيسية بالموقع وسيتم بصفة مستمرة إخطارك بالبيانات التي حصلنا عليها , وكيف سنستخدمها والجهة التي سنقوم بتزويدها بهذه البيانات. </p>\n" +
            " \n";

    public static final String HTML_TEXT_ABOUT_US="    <h1>عن تطبيقنا</h1>\n" +
            "    <p>مؤسسة تعليمية تعمل على إيصال المعرفة بكل الطرق والأساليب لتلائم جميع الفئات. بدأ أخضر منذ أربع أعوام على يوتيوب\n" +
            "        وقدم خلالها العديد من الملخصات لمئات الكتب. كما تم إطلاق تطبيق أخضر والذي من خلاله يمكنك الإستماع إلى أفكار أهم\n" +
            "        الكتب في العالم بـ 15د فقط</p><br>\n" +
            "\n" +
            "    <h3>الرسالة</h3>\n" +
            "    <br>\n" +
            "    <p>نشر العلم بهدف زيادة الوعي وكفاءة الفرد في المجتمع ومساعدته على توسيع آفاقه في شتى مجالات الحياة عن طريق تلخيص\n" +
            "        أهم الكتب بكثير من المجالات في مدة زمنية قصيرة.</p>\n" +
            "    <h3>الفريق</h3>\n" +
            "    <br>\n" +
            "    <p>تعمل كل الفرق في جميع الأقسام على توفير تجربة استخدام متطورة وإنتاج أفضل المحتوى./ يضم أخضر العشرات من أفضل\n" +
            "        القراء في جميع المجالات يعملون دائما على إيجاد أفضل العناوين وأبرز الأفكار</p>\n" +
            "    <h1>أهدافنا</h1>\n" +
            "    <br>\n" +
            "    <h3>توفير الوقت</h3>\n" +
            "    <br>\n" +
            "    <p>بأي وقت وفي أي مكان يمكنك الإستماع لملخصات أخضر ب15د فقط / لم يعد الوقت عائق في تحصيل المعرفة، ب15د فقط يمكنك\n" +
            "        الحصول على أهم أفكار الكتب الأفضل في شتى المجالات.</p>\n" +
            "\n" +
            "    <h3>لا تتوقف عن التعلم</h3>\n" +
            "    <br>\n" +
            "    <p>قمنا بتصميم التطبيق مع مراعاة أنظمة تفاعلية تحافظ على حماسك واستمراريتك في التعلم</p>\n" +
            "\n" +
            "    <h3>محتوى متنوع</h3>\n" +
            "    <br>\n" +
            "    <p>من خلال تطبيق أخضر يمكنك الوصول لآلاف الأفكار في مئات الكتب وبأكثر من 16 قسم.\n" +
            "\n";

}