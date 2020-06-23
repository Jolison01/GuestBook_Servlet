package Dao;

import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    private String url;
    private String user;
    private String password;
    private Connection connection;


    public MessageDao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connection = getConnection();


    }

    public List<Message> getAllMessages() {
            List<Message> messages = new ArrayList<>();
            try{
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM GuestBook");
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()){

                    messages.add(new Message(resultSet.getTimestamp("date").toLocalDateTime()
                    , resultSet.getString("name")
                    , resultSet.getString("message")));
                    statement.close();
                }

            }catch (SQLException ex){
                ex.printStackTrace();
            }

            return messages;
    }



    public void createMessage(Message message){

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO GuestBooK(Date, Name, Message) VALUES(?,?,?) ");
            statement.setTimestamp(1, Timestamp.valueOf(message.getDate()));
            statement.setString(2,message.getName());
            statement.setString(3,message.getMessage());
            statement.execute();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Connection getConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                System.out.printf("something went wrong during connection");
                ex.printStackTrace();
            }
        }
        return connection;
    }


    public void closeConnection(){

    try {
        connection.close();
    }catch (SQLException e){
        e.printStackTrace();
    }
    }

}
