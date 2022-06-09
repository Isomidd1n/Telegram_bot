package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.enums.AdminStatus;
import com.company.enums.Data;
import com.company.model.Product;
import com.company.service.CategoryService;
import com.company.service.ProductService;
import com.company.util.DemoUtil;
import com.company.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class AdminController {
    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasContact()) {
            handleContact(user, message);
        } else if (message.hasPhoto()) {
            handlePhoto(user, message);
        }
    }

    private void handlePhoto(User user, Message message) {
        List<PhotoSize> photoSizeList = message.getPhoto();

        String chatId = String.valueOf(message.getChatId());

        if (ComponentContainer.productStepMap.containsKey(chatId)) {
            Product product = ComponentContainer.productMap.get(chatId);

            if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.ENTERED_PRODUCT_QUANTITY)) {
                product.setImage(photoSizeList.get(photoSizeList.size() - 1).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format("Kategoriya: %s\n  ID : %s \n" +
                                "Mahsulot: %s \n Narxi $: %s $\n Soni: %s ta \n Tafsiloti:  %s \n\n Quyidagi mahsulot bazaga qo'shilsinmi?",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                sendPhoto.setReplyMarkup(InlineKeyboardUtil.confirmAddProductMarkup());

                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
            }
        }
    }

    private void handleContact(User user, Message message) {

    }

    private void handleText(User user, Message message) {
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));


        if (text.equals("/start")) {
            sendMessage.setText("Amalni tanlang:");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
        } else if (ComponentContainer.productStepMap.containsKey(chatId)) {

            Product product = ComponentContainer.productMap.get(chatId);
            Integer id = null;
            String newName = null;
            Double newPrice = null;
            Double newQuantity = null;

            if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.SELECT_CATEGORY_FOR_ADD_PRODUCT)) {
                product.setName(text);
                ComponentContainer.productStepMap.put(chatId, AdminStatus.ENTERED_PRODUCT_NAME);

                sendMessage.setText("Mahsulot narxini kiriting(haqiqiy musbat son)$: ");

            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.ENTERED_PRODUCT_NAME)) {
                double price = 0;
                try {
                    price = Double.parseDouble(text.trim());
                } catch (NumberFormatException e) {
                    sendMessage.setText("Narx noto'g'ri kiritildi, Qaytadan narxni kiriting $: ");
                }

                if (price <= 0) {
                    sendMessage.setText("Narx noto'g'ri kiritildi, Qaytadan narxni kiriting $: ");
                } else {
                    product.setPrice(price);
                    ComponentContainer.productStepMap.put(chatId, AdminStatus.ENTERED_PRODUCT_PRICE);

                    sendMessage.setText("Mahsulotning tafsilotlarini yozing: ");
                }
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.ENTERED_PRODUCT_PRICE)) {
                product.setDescription(text);
                ComponentContainer.productStepMap.put(chatId, AdminStatus.ENTERED_PRODUCT_DESCRIPTION);

                sendMessage.setText("Mahsulot sonini kiriting: ");
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.ENTERED_PRODUCT_DESCRIPTION)) {
                try {
                    product.setQuantity(Integer.valueOf(text));

                    ComponentContainer.productStepMap.put(chatId, AdminStatus.ENTERED_PRODUCT_QUANTITY);

                    sendMessage.setText("Mahsulot rasmini jo'nating: ");
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqattan urinib ko'ring.");
                }
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.DELETE_PRODUCT)) {
                try {
                    ProductService.deleteProduct(Integer.valueOf(text));
                    sendMessage.setText("Mahsulot o'chirildi ! ");

                    ComponentContainer.productMap.remove(chatId);
                    ComponentContainer.productStepMap.remove(chatId);

                    sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqattan urinib ko'ring.");
                }
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_NAME)) {
                try {
                    id = Integer.valueOf(text);

                    ComponentContainer.crudStepMap.put(chatId, new Product(id));
                    ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_NAME_BY_ID);
                    sendMessage.setText("Mahsulotning yangi nomini kiriting: ");
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }

            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_NAME_BY_ID)) {
                newName = text;
                Product product1 = ComponentContainer.crudStepMap.get(chatId);
                ProductService.updateProductName(product1.getId(), newName);

                ComponentContainer.crudStepMap.remove(chatId);

                sendMessage.setText("Nomi o'zgartirildi !");
                sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_DESCRIPTION)) {
                try {
                    id = Integer.valueOf(text);
                    ComponentContainer.crudStepMap.put(chatId, new Product(id));
                    ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_DESCRIPTION_BY_ID);
                    sendMessage.setText("Mahsulotning yangi tafsilotini kiriting: ");
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }

            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_DESCRIPTION_BY_ID)) {
                newName = text;
                Product product1 = ComponentContainer.crudStepMap.get(chatId);
                ProductService.updateProductDescription(product1.getId(), newName);

                ComponentContainer.crudStepMap.remove(chatId);

                sendMessage.setText("Tafsilotlari o'zgartirildi ✔");
                sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_PRICE)) {
                try {
                    id = Integer.valueOf(text);
                    ComponentContainer.crudStepMap.put(chatId, new Product(id));
                    ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_PRICE_BY_ID);
                    sendMessage.setText("Mahsulotning yangi narxini kiriting $: ");
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }

            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_PRICE_BY_ID)) {
                try {
                    newPrice = Double.valueOf(text);
                    Product product1 = ComponentContainer.crudStepMap.get(chatId);
                    ProductService.updateProductPrice(newPrice, product1.getId());

                    ComponentContainer.crudStepMap.remove(chatId);

                    sendMessage.setText("Narxi o'zgartirildi ✔");
                    sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_QUANTITY)) {
                try {
                    id = Integer.valueOf(text);

                    ComponentContainer.crudStepMap.put(chatId, new Product(id));
                    ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_QUANTITY_BY_ID);
                    sendMessage.setText("Mahsulotning yangi sonini kiriting: ");
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }


            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.CHANGE_QUANTITY_BY_ID)) {
                try {
                    newQuantity = Double.valueOf(text);
                    Product product1 = ComponentContainer.crudStepMap.get(chatId);
                    ProductService.updateProductQuantity(newQuantity, product1.getId());

                    ComponentContainer.crudStepMap.remove(chatId);

                    sendMessage.setText("Soni o'zgartirildi ✔");
                    sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }
            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.SWITCH_OFF_PRODUCT)) {
                try {
                    ProductService.updateProductDeletedOff(Integer.valueOf(text));
                    sendMessage.setText("Mahsulot o'chirildi ✔ \n\n Amalni Tanlang ");

                    ComponentContainer.productMap.remove(chatId);
                    ComponentContainer.productStepMap.remove(chatId);
                    sendMessage.setReplyMarkup(InlineKeyboardUtil.updateProduct());
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }


            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.SWITCH_ON_PRODUCT)) {
                try {
                    ProductService.updateProductDeletedOn(Integer.valueOf(text));
                    sendMessage.setText("Mahsulot yoqildi ✔ \n\n Amalni Tanlang ");

                    sendMessage.setReplyMarkup(InlineKeyboardUtil.updateProduct());
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi iltimos boshqatdan urinib ko'ring.");
                }
            }

        } else if (text.equals("/menu")) {
            sendMessage.setText("Quyidagilardan birini tanlang: ");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
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

        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }


    public void handleCallBack(User user, Message message, String data) {
        String chatId = String.valueOf(message.getChatId());

        if (data.equals("add_product")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Kategoriyalardan birini tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.categoryInlineMarkup());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            ComponentContainer.productMap.remove(chatId);
            ComponentContainer.productStepMap.remove(chatId);

            ComponentContainer.productStepMap.put(chatId, AdminStatus.CLICKED_ADD_PRODUCT);
            ComponentContainer.productMap.put(chatId,
                    new Product(null, null, null, null, null, null, null, false));

        } else if (data.startsWith("add_product_category_id")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            int categoryId = Integer.parseInt(data.split("/")[1]);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Mahsulot nomini kiriting : "
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

            ComponentContainer.productStepMap.put(chatId, AdminStatus.SELECT_CATEGORY_FOR_ADD_PRODUCT);
            Product product = ComponentContainer.productMap.get(chatId);
            product.setCategoryId(categoryId);
        } else if (data.equals(String.valueOf(Data.ADD_PRODUCT_COMMIT))) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            Product product = ComponentContainer.productMap.get(chatId);

            ProductService.addProduct(product);

            ComponentContainer.productMap.remove(chatId);
            ComponentContainer.productStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, product.getName() + " saqlandi✔.\n\n" + "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals(String.valueOf(Data.ADD_PRODUCT_CANCEL))) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ComponentContainer.productMap.remove(chatId);
            ComponentContainer.productStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("show_product_list")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductList();


            SendMessage sendMessage = new SendMessage(
                    chatId, "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.showList());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("show_true")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.deletedProductList();

            for (Product product : Database.productList) {

                if (product.isDeleted()) {
                    System.out.println("product.getImage() = " + product.getImage());
                    SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                    sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                    "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                            CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                            product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                    ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
                }
            }

            SendMessage sendMessage = new SendMessage(
                    chatId, "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("show_false")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.notDeletedProductList();


            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }
            SendMessage sendMessage = new SendMessage(
                    chatId, "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("show_all")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadAllProductList();

            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }

            SendMessage sendMessage = new SendMessage(
                    chatId, "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.showList());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("delete_product")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductList();

            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }

            SendMessage sendMessage = new SendMessage(chatId, "O'chirmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.DELETE_PRODUCT);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("change_name")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductList();

            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }

            SendMessage sendMessage = new SendMessage(chatId, "Nomini o'zgartirmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_NAME);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("change_price")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductList();

            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }

            SendMessage sendMessage = new SendMessage(chatId, "Narxini o'zgartirmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_PRICE);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals(DemoUtil.BackForAdmin)) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Assosiy Menu  :\n\n Pastagilardan birortasini tanlang 🔽");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("change_description")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductList();
            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }

            SendMessage sendMessage = new SendMessage(chatId, "Tafsilotini o'zgartirmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_DESCRIPTION);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("change_quantity")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductList();
            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }

            SendMessage sendMessage = new SendMessage(chatId, "Sonini o'zgartirmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.CHANGE_QUANTITY);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("update_product")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Assosiy CRUD Menu  :\n\n Pastagilardan birortasini tanlang 🔽");

            sendMessage.setReplyMarkup(InlineKeyboardUtil.updateProduct());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("change_deleted_on")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            ProductService.loadProductListUpdate();

            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }


            SendMessage sendMessage = new SendMessage(chatId, "Yoqmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.SWITCH_ON_PRODUCT);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("change_deleted_off")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);


            ProductService.loadProductList();
            for (Product product : Database.productList) {


                System.out.println("product.getImage() = " + product.getImage());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format(" Kategoriya: %s\n ID: %s\n " +
                                "Mahsulot: %s\n Narxi $: %s\n Soni: %s\n Tafsiloti: %s\n",
                        CategoryService.getCategoryById(product.getCategoryId()).getName(), product.getId(),
                        product.getName(), product.getPrice(), product.getQuantity(), product.getDescription()));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

            }


            SendMessage sendMessage = new SendMessage(chatId, "O'chirmoqchi bo'lgan mahsulotning ID sini kiriting: ");
            ComponentContainer.productStepMap.put(chatId, AdminStatus.SWITCH_OFF_PRODUCT);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("back_crud")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Assosiy CRUD Menu  :\n\n Pastagilardan birortasini tanlang 🔽");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("show_back")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);
            SendMessage sendMessage = new SendMessage(chatId, "Amalni tanlang");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if (data.equals("user_list")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            FileController.getCustomersPDF();
            SendMessage sendMessage = new SendMessage(chatId, "FOYDALANUVCHILAR RO'YXATI");
            SendDocument sendDocument = new SendDocument();
            sendDocument.setDocument(new InputFile(requireNonNull(FileController.getCustomersPDF())));
            sendDocument.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendDocument);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals("sold_list")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            FileController.generateOrderPDF();
            SendMessage sendMessage = new SendMessage(chatId, "SAVATCHA RO'YXATI");
            SendDocument sendDocument = new SendDocument();
            sendDocument.setDocument(new InputFile(requireNonNull(FileController.generateOrderPDF())));
            sendDocument.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendDocument);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        } else if (data.equals("operation_list")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            FileController.getOperationsPDF();
            SendMessage sendMessage = new SendMessage(chatId, "OPERATSIYALAR RO'YXATI");
            SendDocument sendDocument = new SendDocument();
            sendDocument.setDocument(new InputFile(requireNonNull(FileController.getOperationsPDF())));
            sendDocument.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardUtil.productAdminMenu());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendDocument);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }

    }


    public void notificationToAdmin(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ComponentContainer.ADMIN_ID);
        sendMessage.setText(message);
        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }
}
