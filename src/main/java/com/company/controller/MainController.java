package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.enums.CustomerStatus;
import com.company.enums.Data;
import com.company.model.*;
import com.company.service.*;
import com.company.util.DemoUtil;
import com.company.util.InlineKeyboardUtil;
import com.company.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MainController {
    public AdminController adminController = new AdminController();

    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasContact()) {
            handleContact(user, message);
        } else if (message.hasLocation()) {
            handleLocation(user, message);
        }
    }

    private void handleLocation(User user, Message message) {

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(true, true);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(
                "Buyurtma qabul qilindi !!!"
        );

        sendMessage.setReplyMarkup(replyKeyboardRemove);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        String chatId = String.valueOf(message.getChatId());

        Location locationUser = message.getLocation();
        Location locationGeneral = new Location();

        locationGeneral.setLatitude(41.32647926254397);
        locationGeneral.setLongitude(69.22848447454385);

//41.32647926254397, 69.22848447454385  -> PDP ACADEMY

        Double latitude1 = locationGeneral.getLatitude();
        Double longitude1 = locationGeneral.getLongitude();

        Double latitude2 = locationUser.getLatitude();
        Double longitude2 = locationUser.getLongitude();

//Find the value of the 'latitude' in radians:
//Value of Latitude in Radians, lat = Latitude / (180/pi) OR
//Value of Latitude in Radians, lat = Latitude / 57.29577951

//Find the value of 'longitude' in radians:
//Value of Longitude in Radians, long = Longitude / (180/pi) OR
//Value of Longitude in Radians, long = Longitude / 57.29577951
//Distance(Mille), d = 3963.0 * arccos[(sin(lat1) * sin(lat2)) + cos(lat1) * cos(lat2) * cos(long2 – long1)]
//Daistance ->   km = (mille * 1.609344)

        double lat1 = (latitude1 / 57.29577951);
        double lat2 = (latitude2 / 57.29577951);
//        double lat1 = (latitude1 / 39.647346);
//        double lat2 = (latitude2 / 39.647346);
//
//        Double lon2 = (longitude1 / 64.070486);
//        Double lon1 = (longitude2 / 64.070486);
        Double lon1 = (longitude1 / 57.29577951);
        Double lon2 = (longitude2 / 57.29577951);

        double distanceMile = 3963.0 * Math.acos((sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)));

        double distanceKm = (distanceMile * 1.609344);

        if (distanceKm > 35.675401373277) {

            sendMessage.setText("Yetkazib berish xizmati faqat <b>Toshkent</b> shaxrida amal qiladi❗️❗️❗️");
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else {

            CartProductService.loadCartProductList();

            CustomerService.loadCustomerList();

            List<CartProduct> cartProductList = CustomerService.getCartProducts(String.valueOf(user.getId()));

            double total = 0;
            LocalDateTime dateTime = LocalDateTime.now();


            StringBuilder messageText = new StringBuilder("🛒 Buyurtmada: \n\n");

            for (CartProduct cartProduct : cartProductList) {
                Product product = ProductService.getProductById(cartProduct.getProductId());
                messageText.append(product.getName()).append("\n");
                messageText.append(cartProduct.getQuantity()).append(" * ").append(product.getPrice());
                messageText.append(" = ").append(cartProduct.getQuantity() * product.getPrice()).append("\n\n");

                total += (cartProduct.getQuantity() * product.getPrice());
                messageText.append("\n\uD83D\uDCC5 Buyurtma vaqti: ");
                messageText.append(dateTime);
            }
            messageText.append("\n\n\uD83D\uDCB8 Jami mahsulotlar: ");
            messageText.append(total);


            Long userId = user.getId();
            String userFirstName = user.getFirstName();
            String userLastName = user.getLastName();

            Customer customerById = CustomerService.getCustomerById(String.valueOf(userId));

            String phoneNumber = customerById.getPhoneNumber();

            LocalDateTime localDateTime = LocalDateTime.now();


            Order order = ComponentContainer.orderMap.get(chatId);

            order.setUser_id(String.valueOf(userId));
            order.setFirst_name(userFirstName);
            order.setLast_name(userLastName);
            order.setPhone_number(phoneNumber);
            order.setTotalprice(total);
            order.setUser_order(String.valueOf(messageText));
            order.setLocalDateTime(String.valueOf(localDateTime));

            OrderService.addOrder(order);

            String msg = String.format("Buyurtma mavjud!!!\n" +
                    "ID: %s\nBuyurtma qilivchi: <b>%s %s</b>\n" +
                    "Telefon raqami: <b>%s</b>\n\n" +
                    "Buyurtma: \n<b>%s</b>\n", userId, userFirstName, userLastName, phoneNumber, messageText);
            SendMessage sendMessageAdmin = new SendMessage();
            sendMessageAdmin.setText(msg);
            sendMessageAdmin.setParseMode(ParseMode.HTML);
            sendMessageAdmin.setChatId(ComponentContainer.ADMIN_ID);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessageAdmin);

            SendContact sendContact = new SendContact();
            sendContact.setProtectContent(true);
            sendContact.setPhoneNumber(phoneNumber);
            sendContact.setFirstName(userFirstName);
            sendContact.setLastName(userLastName);
            sendContact.setDisableNotification(true);
            sendContact.setChatId(ComponentContainer.ADMIN_ID);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendContact);


            CartProductService.deleteCartProduct(chatId);

            SendLocation sendLocation = new SendLocation();
            sendLocation.setLatitude(latitude2);
            sendLocation.setLongitude(longitude2);
            sendLocation.setChatId(ComponentContainer.ADMIN_ID);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendLocation);

            sendMessage.setChatId(chatId);
            sendMessage.setText(userFirstName + " siz bilan tez orada Aloqaga chiqamiz \n Harindingiz uchun Rahmat 😊");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }

    }

    private void handleContact(User user, Message message) {
        Contact contact = message.getContact();
        String customerId = String.valueOf(contact.getUserId());

        Customer customer = CustomerService.getCustomerById(customerId);
        if (customer == null) {

            customer = new Customer(customerId, contact.getFirstName(),
                    contact.getLastName(), contact.getPhoneNumber(), CustomerStatus.SHARE_CONTACT);

            CustomerService.addCustomer(customer);

            SendMessage sendMessage = new SendMessage();
            // sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            sendMessage.setChatId(customerId);
            sendMessage.setText("<b>🚗Assalomu Aleykum " + user.getFirstName() + " Torrent Botiga Hush Kelibsiz ! \n\n Menudan Birortasini Tanlang</b>\n");
            sendMessage.setParseMode(ParseMode.HTML);
            InlineKeyboardMarkup menu = InlineKeyboardUtil.Menu();
            sendMessage.setReplyMarkup(menu);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }
    }

    private void handleText(User user, Message message) {
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Customer customer = CustomerService.getCustomerById(String.valueOf(message.getChatId()));

        if (text.equals("/start")) {

            if (customer == null) {
                String messageText = String.format("ID: %s\nFirst name:%s\n" +
                                "Last name: %s\nUsername: %s\n\nSTART BOSDI .",
                        user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
                adminController.notificationToAdmin(messageText);
                sendMessage.setText("<b>Assalomu Aleykum!</b>\n" +
                        "Raqamingizmi jo'nating.");
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setReplyMarkup(KeyboardUtil.contactMarkup());
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            } else {
                sendMessage.setText("<b>💻Assalomu Aleykum " + user.getFirstName() + " torrent botiga xush kelibsiz ! \n\n Menudan Birortasini Tanlang</b>\n");
                sendMessage.setParseMode(ParseMode.HTML);

                sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
            if (text.equals("/help")) {
                sendMessage.setText("Yordam kerak bo'lsa [Admin](https://t.me/isomiddin_999)ga murojaat qiling.");
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            } else if (text.equals("/about")) {
                sendMessage.setText("Assalomu aleykum *" + user.getUserName() + "* botimiz asosan chet el mashinalarini sotish bilan shug'ullanadi.\n" +
                        "Bizni *ijtimoiy tarmoqlarda kuzatib boring👇*\n" +
                        "Instagram: [bosing](https://www.instagram.com/isomiddin_39/)\n" +
                        "Telegram: [bosing](https://t.me/Farmingsimulatormod)\n" +
                        "YouTube: [bosing](https://www.youtube.com/channel/UCNL-dFBJ-B1HaWs3fE2Rfjw)\n" +
                        "Facebook: [bosing](https://www.facebook.com/profile.php?id=100069065493562)");
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }

        } else if (text.equals("/menu")) {
            sendMessage.setText("Quyidagilardan birini tanlang:👇 ");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }else if (text.equals("/help")) {
            sendMessage.setText("Yordam kerak bo'lsa [Admin](https://t.me/isomiddin_999)ga murojaat qiling.");
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (text.equals("/about")) {
            sendMessage.setText("Assalomu aleykum *" + user.getUserName() + "* botimiz asosan chet el mashinalarini sotish bilan shug'ullanadi.\n" +
                    "Bizni *ijtimoiy tarmoqlarda kuzatib boring👇*\n" +
                    "Instagram: [bosing](https://www.instagram.com/isomiddin_39/)\n" +
                    "Telegram: [bosing](https://t.me/Farmingsimulatormod)\n" +
                    "YouTube: [bosing](https://www.youtube.com/channel/UCNL-dFBJ-B1HaWs3fE2Rfjw)\n" +
                    "Facebook: [bosing](https://www.facebook.com/profile.php?id=100069065493562)");
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }


    }


    public void handleCallBack(User user, Message message, String data) {
        String chatId = String.valueOf(message.getChatId());

        if (data.equals(String.valueOf(Data.OUR_CONTACTS))) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "BIZNING IJTIMOIY SAHIFA : ");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.NetAddressMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals(String.valueOf(Data.PRODUCT_MENU))) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Categoriyalardan Birini tanlang :\n\n Pastagilardan birortasini tanlang 🔽 ");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.parentCategoryInlineMarkup());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.startsWith(DemoUtil.BACK_TO_CATEGORY)) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int categoryId = Integer.parseInt(data.split("/")[1]);
            Category category = CategoryService.getCategoryById(categoryId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Category menu  :\n\n Pastagilardan birortasini tanlang 🔽");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.categoryMenu(category.getParentCategoryId()));
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals("back_to_parent_category")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Category menu  :\n\n Pastagilardan birortasini tanlang 🔽");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.parentCategoryInlineMarkup());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        } else if (data.equals("cart")) {
            SendMessage sendMessage = new SendMessage();

            List<CartProduct> cartProductProductList = CartProductService.getCustomerProductList(String.valueOf(user.getId()));
            if (cartProductProductList == null || cartProductProductList.isEmpty()) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("🛒 Savatchangiz hozircha bo`sh");
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            } else {
                DeleteMessage deleteMessage = new DeleteMessage(
                        chatId, message.getMessageId()
                );
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

                double total = 0;
                String messageText = "🛒 Savatchada: \n\n";

                for (CartProduct cartProduct : cartProductProductList) {
                    Product product = ProductService.getProductById(cartProduct.getProductId());

                    messageText += product.getName() + "\n";
                    messageText += cartProduct.getQuantity() + " ✖ " + product.getPrice();
                    messageText += " = " + (cartProduct.getQuantity() * product.getPrice()) + "\n\n";

                    total += (cartProduct.getQuantity() * product.getPrice());

                }
                messageText += "\uD83D\uDCB8 Jami mahsulotlar $: ";
                messageText += total;

                sendMessage.setText(messageText);
                sendMessage.setChatId(chatId);

                sendMessage.setReplyMarkup(InlineKeyboardUtil.cartMenu(cartProductProductList));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        } else if (data.equals("back_from_product_show")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, " Cart Menu  :");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        } else if (data.equals(DemoUtil.BACKForUser)) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Assosiy Menu  :\n\n Pastagilardan birortasini tanlang 🔽");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.startsWith(DemoUtil.REMOVE_CART_PRODUCT)) {
//            List<CartProduct> cartProductProductList = CustomerService.getCartProducts(String.valueOf(user.getId()));

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            String[] split = data.split("/");
            Integer cartProductId = Integer.parseInt(split[1]);

            CartProductService.deleteProduct(cartProductId, chatId);
            //  Database.cartProductList.removeIf(cartProduct -> cartProduct.getId().equals(cartProductId));

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Savatchangiz Hozircha Bo'sh  :\n\n Pastagilardan birortasini tanlang 🔽");

            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

//            handleText(user, DemoUtil.CART_UZ);
        } else if (data.startsWith(DemoUtil.CONTINUE)) {

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
            SendMessage sendMessage = new SendMessage(chatId, " Category dan birini  Tanlang 😊: ");

            sendMessage.setReplyMarkup(InlineKeyboardUtil.parentCategoryInlineMarkup());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.startsWith(DemoUtil.COMMIT)) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);


            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Joylashuv");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardUtil.locationKeyboard());

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.startsWith(DemoUtil.CANCEL)) {

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
            CartProductService.deleteCartProduct(chatId);

            Database.cartProductList.clear();


            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText("Buyurtma bekor qilindi 😔. \n\n" +
                    "Assosiy Menu  :\n\n Pastagilardan birortasini tanlang 🔽");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.startsWith("product_parent_category_id/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int categoryId = Integer.parseInt(data.split("/")[1]);


            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Kategoriya tanlang 😊: ");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.categoryMenu(categoryId));
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        } else if (data.equals(String.valueOf(Data.MAGAZINE))) {
            Location locationGeneral = new Location();

            locationGeneral.setLatitude(41.3264004);
            locationGeneral.setLongitude(69.2285946);

//41.338294895273236, 69.2708062321676  TECHNO CHAIN


            Double latitude1 = locationGeneral.getLatitude();
            Double longitude1 = locationGeneral.getLongitude();

            SendLocation sendLocation = new SendLocation();

            sendLocation.setLatitude(latitude1);
            sendLocation.setLongitude(longitude1);
            sendLocation.setChatId(chatId);


            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendLocation);


        } else if (data.startsWith("product_category_id/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int categoryId = Integer.parseInt(data.split("/")[1]);


            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Product tanlang 😊: ");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productMenu(categoryId));
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);


        } else if (data.startsWith("product/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int productId = Integer.parseInt(data.split("/")[1]);

            Product product = ProductService.getProductById(productId);

            SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
            String caption = "Kategoriya : " + CategoryService.getCategoryById(product.getCategoryId()).getName() + "\n" +
                    "Mahsulot : " + product.getName() + "\n" +
                    "Narxi $: " + product.getPrice() + "\n" +
                    "Mahsulot haqida malumot : " + product.getDescription();
            sendPhoto.setReplyMarkup(InlineKeyboardUtil.countProductMenu(product));
            sendPhoto.setCaption(caption);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

        } else if (data.startsWith("back_from_count_product")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int categoryId = Integer.parseInt(data.split("/")[1]);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Product tanlang 😊:");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productMenu(categoryId));
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.startsWith("count/")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int productId = Integer.parseInt(data.split("/")[1]);
            int quantity = Integer.parseInt(data.split("/")[2]);

            Product product = ProductService.getProductById(productId);


            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            if (product.getQuantity() - quantity >= 0) {
                sendMessage.setText("Haridingiz savatchaga tushdi! 😊\n\n\n Amal tanlang :");
                sendMessage.setReplyMarkup(InlineKeyboardUtil.Menu());

                ComponentContainer.orderMap.put(chatId, new Order(null, null, null, null, null, null, null, null));

                CartProduct cartProduct = new CartProduct(chatId, product.getId(), quantity);
                CartProductService.addCartProduct(cartProduct);
            } else {
                String messageText = String.format(("Uzur bizning bazamizda %s dan %s ta mahsulot yo'q"),
                        product.getName(), quantity);

                sendMessage.setText(messageText);
                sendMessage.setReplyMarkup(InlineKeyboardUtil.back());
            }
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
    }
}
