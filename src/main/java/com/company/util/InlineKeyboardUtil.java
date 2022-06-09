package com.company.util;

import com.company.db.Database;
import com.company.enums.Data;
import com.company.model.CartProduct;
import com.company.model.Category;
import com.company.model.Product;
import com.company.service.CategoryService;
import com.company.service.ProductService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InlineKeyboardUtil {

    public static InlineKeyboardMarkup showList() {
        InlineKeyboardButton allButton = getButton("🧾 Hamma mahsulotlar🧾", "show_all");
        InlineKeyboardButton trueButton = getButton("🟢 O'chirilmagan mahsulotlarni🟢", "show_false");
        InlineKeyboardButton falseButton = getButton("🔴 O'chirilgan mahsulotlarni ko'rish 🔴", "show_true");
        InlineKeyboardButton back = getButton(" ◀ ORTGA", "show_back");

        List<InlineKeyboardButton> row1 = getRow(allButton);
        List<InlineKeyboardButton> row2 = getRow(trueButton);
        List<InlineKeyboardButton> row3 = getRow(falseButton);
        List<InlineKeyboardButton> row4 = getRow(back);


        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2,row3,row4);
        return new InlineKeyboardMarkup(rowList);
    }


    public static InlineKeyboardMarkup productAdminMenu() {
        InlineKeyboardButton addButton = getButton("➕ Mahsulot qo'shish", "add_product");
        InlineKeyboardButton updateButton = getButton("⏫ Mahsulot o'zgartirish", "update_product");
        InlineKeyboardButton deleteButton = getButton("✖ Mahsulot o'chirish", "delete_product");
        InlineKeyboardButton listButton = getButton("📜 Mahsulotlar ro'yxati", "show_product_list");
        InlineKeyboardButton operationButton = getButton("📋 Operatsiyalar ro'yxati", "operation_list");
        InlineKeyboardButton customersButton = getButton("👨‍👦‍👦 Mijozlar ro'yxati", "user_list");
        InlineKeyboardButton soldButton = getButton(" 💵 Sotuv Bo'limi", "sold_list");

        List<InlineKeyboardButton> row1 = getRow(addButton, updateButton);
        List<InlineKeyboardButton> row2 = getRow(deleteButton, listButton);
        List<InlineKeyboardButton> row3 = getRow(operationButton, customersButton);
        List<InlineKeyboardButton> row4 = getRow(soldButton);


        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3, row4);
        return new InlineKeyboardMarkup(rowList);
    }

    public static InlineKeyboardMarkup NetAddressMenu() {
        InlineKeyboardButton instagram = getButton("📤 Instgram", String.valueOf(Data.INSTAGRAM));
        InlineKeyboardButton telegram = getButton("📤 Telegram", String.valueOf(Data.TELEGRAM));
        InlineKeyboardButton facebook = getButton("📤 Facebook", String.valueOf(Data.FACEBOOK));
        InlineKeyboardButton webAddres = getButton("🌐 WebAddress", String.valueOf(Data.WEB));
        InlineKeyboardButton youTube = getButton("🎥 You Tube", String.valueOf(Data.YOU_TUBE));
        InlineKeyboardButton back = getButton("◀ BACK", DemoUtil.BACKForUser);

        instagram.setUrl("https://www.instagram.com/isomiddin_39/");
        telegram.setUrl("https://t.me/Farming_simulator_20");
        facebook.setUrl("https://www.facebook.com/profile.php?id=100069065493562");
        youTube.setUrl("https://www.youtube.com/channel/UCNL-dFBJ-B1HaWs3fE2Rfjw");
        webAddres.setUrl("https://www.cadillac.com/electric/lyriq");


        List<InlineKeyboardButton> row1 = getRow(instagram, facebook);
        List<InlineKeyboardButton> row2 = getRow(telegram, youTube);
        List<InlineKeyboardButton> row3 = getRow(webAddres);
        List<InlineKeyboardButton> row4 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3, row4);
        return new InlineKeyboardMarkup(rowList);
    }


    public static InlineKeyboardMarkup Menu() {
        InlineKeyboardButton menuButton = getButton("📠 PRODUCT MENU", String.valueOf(Data.PRODUCT_MENU));
        InlineKeyboardButton magazineButton = getButton("🏪 LOCATION", String.valueOf(Data.MAGAZINE));
        InlineKeyboardButton contactButton = getButton("📞OUR CONTACTS", String.valueOf(Data.OUR_CONTACTS));
        InlineKeyboardButton cartButton = getButton("🛒 Cart ", "cart");

//        magazineButton.setUrl("https://www.google.com/maps/place/Nout.uz+-+%D0%9C%D0%B0%D0%B3%D0%B0%D0%B7%D0%B8%D0%BD+%D0%9D%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA%D0%BE%D0%B2+N1/@41.3386395,69.2723408,15z/data=!4m5!3m4!1s0x0:0x7e8c159c995773ea!8m2!3d41.3386395!4d69.2723408");

        List<InlineKeyboardButton> row1 = getRow(menuButton);
        List<InlineKeyboardButton> row2 = getRow(magazineButton, contactButton);
        List<InlineKeyboardButton> row3 = getRow(cartButton);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);
    }


    public static InlineKeyboardMarkup updateProduct() {
        InlineKeyboardButton change_name = getButton("🖊 Nomini 🖊", "change_name");
        InlineKeyboardButton change_price = getButton("💸 Narxini 💸", "change_price");
        InlineKeyboardButton change_description = getButton("📜 Tafsilotini 📜", "change_description");
        InlineKeyboardButton change_quantity = getButton("📜 Sonini 📜", "change_quantity");
        InlineKeyboardButton change_deleted = getButton("📑 O'chirilganlarini Yoqish  📑", "change_deleted_on");
        InlineKeyboardButton change_deleted_switch = getButton("📑 Yoqilganlarni o'chirish  📑", "change_deleted_off");
//        InlineKeyboardButton change_deleted_show = getButton("📜 O'chirilganlarini Ko'rish  📜", "change_deleted_show");
//        InlineKeyboardButton show_all_products = getButton("📜 Hamma Mahsulotlarni Ko'rish  📜", "show_all_products");
        InlineKeyboardButton back = getButton("◀ Orqaga ", "back_crud");


        List<InlineKeyboardButton> row1 = getRow(change_name, change_price);
        List<InlineKeyboardButton> row2 = getRow(change_description, change_quantity);
        List<InlineKeyboardButton> row3 = getRow(change_deleted, change_deleted_switch);
//        List<InlineKeyboardButton> row4 = getRow(show_all_products);
        List<InlineKeyboardButton> row5 = getRow(back);


        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3 , row5);

        return new InlineKeyboardMarkup(rowList);
    }


    public static InlineKeyboardMarkup categoryMenu(Integer parentCategoryId) {

        CategoryService.loadCategoryListForUsers();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (Category category : Database.categoryList) {
            if (category.getParentCategoryId().equals(parentCategoryId)) {
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton("💻 " + category.getName());
                button.setCallbackData("product_category_id/" + category.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        InlineKeyboardButton back = getButton("◀ BACK", "back_to_parent_category");
        List<InlineKeyboardButton> row1 = getRow(back);
        rowList.add(row1);

        return getInlineMarkup(rowList);

    }

    public static InlineKeyboardMarkup countProductMenu(Product product) {
        String ending = " ta";
        ProductService.loadProductList();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (int i = 1; i <= 7; i += 3) {
            InlineKeyboardButton button1 = getButton(i + ending, "count/" + product.getId() + "/" + i);
            InlineKeyboardButton button2 = getButton((i + 1) + ending, "count/" + product.getId() + "/" + (i + 1));
            InlineKeyboardButton button3 = getButton((i + 2) + ending, "count/" + product.getId() + "/" + (i + 2));

            List<InlineKeyboardButton> row = getRow(button1, button2, button3);

            rowList.add(row);
        }

        InlineKeyboardButton back = getButton(
                "Back",
                "back_from_count_product" + "/" + product.getCategoryId()
        );
        List<InlineKeyboardButton> row = getRow(back);
        rowList.add(row);

        return getInlineMarkup(rowList);
    }


    public static InlineKeyboardMarkup parentCategoryInlineMarkup() {

        CategoryService.loadCategoryList();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (Category category : Database.categoryList) {
            if (category.getParentCategoryId() == 0) {
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton("🚘 " + category.getName());
                button.setCallbackData("product_parent_category_id/" + category.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        InlineKeyboardButton quran = getButton("🕋 QURAN", DemoUtil.Quran);
        quran.setUrl("https://t.me/qur0n_tinglash_bot");
        List<InlineKeyboardButton> row2 = getRow(quran);
        rowList.add(row2);

        InlineKeyboardButton back = getButton("◀ BACK", DemoUtil.BACKForUser);
        List<InlineKeyboardButton> row1 = getRow(back);
        rowList.add(row1);

        return getInlineMarkup(rowList);
    }

    public static InlineKeyboardMarkup productMenu(Integer categoryId) {

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        ProductService.loadProductList();

        for (Product product : Database.productList) {

            if (product.getCategoryId().equals(categoryId)) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("\uD83D\uDC68\uD83C\uDFFE\u200D\uD83D\uDCBB " + product.getName());
                button.setCallbackData("product/" + product.getId());
                List<InlineKeyboardButton> row = getRow(button);
                rowList.add(row);

            }
        }

        InlineKeyboardButton back = getButton("◀ BACK",
                DemoUtil.BACK_TO_CATEGORY+"/"+categoryId);
        List<InlineKeyboardButton> row1 = getRow(back);
        rowList.add(row1);

        return new InlineKeyboardMarkup(rowList);
    }


    private static InlineKeyboardButton getButton(String demo, String data) {
        InlineKeyboardButton button = new InlineKeyboardButton(demo);
        button.setCallbackData(data);
        return button;
    }

    private static List<InlineKeyboardButton> getRow(InlineKeyboardButton... buttons) {
        return Arrays.asList(buttons);
    }

    private static List<List<InlineKeyboardButton>> getRowList(List<InlineKeyboardButton>... rows) {
        return Arrays.asList(rows);
    }


    public static InlineKeyboardMarkup categoryInlineMarkup() {

        CategoryService.loadCategoryListForAdmin();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (Category category : Database.categoryList) {
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton("🚘 " + category.getName());
            button.setCallbackData("add_product_category_id/" + category.getId());
            buttonList.add(button);
            rowList.add(buttonList);
        }

        InlineKeyboardButton back = getButton("◀ BACK", DemoUtil.BackForAdmin);
        List<InlineKeyboardButton> row1 = getRow(back);
        rowList.add(row1);
        return getInlineMarkup(rowList);
    }

    public static InlineKeyboardMarkup confirmAddProductMarkup() {

        InlineKeyboardButton commit = getButton("Ha", String.valueOf(Data.ADD_PRODUCT_COMMIT));
        InlineKeyboardButton cancel = getButton("Yo'q", String.valueOf(Data.ADD_PRODUCT_CANCEL));

        return new InlineKeyboardMarkup(getRowList(getRow(commit, cancel)));
    }

    public static InlineKeyboardMarkup paymentMenu() {
        InlineKeyboardButton UzCard = getButton("💷 UzCard 💷", DemoUtil.UZCARD);
        InlineKeyboardButton HumoCard = getButton("💷 HUMO 💷", DemoUtil.HUMO);
        InlineKeyboardButton MasterCard = getButton("💷 Master Card 💷", DemoUtil.MASTER);
        InlineKeyboardButton VisaCard = getButton("💷 Visa Card 💷", DemoUtil.VISA);
        InlineKeyboardButton back = getButton(" ◀ Orqaga", "back_from_product_show");
        List<InlineKeyboardButton> row1 = getRow(UzCard, HumoCard);
        List<InlineKeyboardButton> row2 = getRow(MasterCard, VisaCard);
        List<InlineKeyboardButton> row3 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);
        return getInlineMarkup(rowList);

    }


    public static InlineKeyboardMarkup cartMenu(List<CartProduct> cartProductProductList) {
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        int product = 1;
        for (CartProduct cartProduct : cartProductProductList) {

            InlineKeyboardButton button = new InlineKeyboardButton("❌ Mahsulot " + product);
            product++;
            button.setCallbackData("remove_cart_product/" + cartProduct.getId());

            List<InlineKeyboardButton> row = getRow(button);

            rowList.add(row);
        }

        InlineKeyboardButton continueButton = getButton(
                ("🔁 Davom Etish "), DemoUtil.CONTINUE);
        List<InlineKeyboardButton> row1 = getRow(continueButton);

        InlineKeyboardButton commitButton = getButton(
                "✔ Tasdiqlash ", DemoUtil.COMMIT);
        InlineKeyboardButton cancelButton = getButton(
                "🚫 Bekor Qilish ", DemoUtil.CANCEL);
        List<InlineKeyboardButton> row2 = getRow(commitButton, cancelButton);

        rowList.add(row1);
        rowList.add(row2);

        return new InlineKeyboardMarkup(rowList);
    }

    public static InlineKeyboardMarkup back() {

        InlineKeyboardButton back = getButton("◀ BACK", DemoUtil.BACKForUser);
        List<InlineKeyboardButton> row1 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row1);

        return new InlineKeyboardMarkup(rowList);
    }

    public static InlineKeyboardMarkup getInlineMarkup(List<List<InlineKeyboardButton>> keyboard) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

}
