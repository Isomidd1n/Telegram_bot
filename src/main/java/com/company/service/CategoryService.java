package com.company.service;

import com.company.db.Database;
import com.company.model.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoryService {
    public static Category getCategoryById(Integer id){

        loadCategoryList();

        for (Category category : Database.categoryList) {
            if(category.getId().equals(id)){
                return category;
            }
        }
        return null;
    }

    public static void loadCategoryListForUsers() {
        Connection connection = Database.getConnection();
        if(connection != null){

            try (Statement statement = connection.createStatement()) {

                Database.categoryList.clear();

                String query = " SELECT * FROM category WHERE parent_category IS NOT NULL ; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    boolean deleted = resultSet.getBoolean("deleted");
                    int parentCategoryId = resultSet.getInt("parent_category");

                    Category category = new Category(id,name, parentCategoryId,deleted);

                    Database.categoryList.add(category);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }

    public static void loadCategoryList() {
        Connection connection = Database.getConnection();
        if(connection != null){

            try (Statement statement = connection.createStatement()) {

                Database.categoryList.clear();

                String query = " SELECT * FROM category WHERE NOT deleted ; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    boolean deleted = resultSet.getBoolean("deleted");
                    int parentCategoryId = resultSet.getInt("parent_category");

                    Category category = new Category(id,name, parentCategoryId,deleted);

                    Database.categoryList.add(category);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }
    public static void loadCategoryListForAdmin() {
        Connection connection = Database.getConnection();
        if(connection != null){

            try (Statement statement = connection.createStatement()) {

                Database.categoryList.clear();

                String query = " SELECT * FROM category WHERE parent_category IS NOT NULL ; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    boolean deleted = resultSet.getBoolean("deleted");

                    Category category = new Category(id,name,deleted);

                    Database.categoryList.add(category);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }
}
