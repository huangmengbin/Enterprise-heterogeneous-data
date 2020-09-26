package china.se.integrate.integration.data;

import china.se.integrate.integration.entity.TableHead;
import org.apache.ibatis.annotations.Mapper;

import java.sql.*;

/**
 * @author hmb
 * 最好的方法是使用连接池，不要每次查询都connect,close,浪费时间
 * 但我们主要是为了集成，即灵活性要求高，各种表字段未知?
 * 公司内部使用，并发度低，所以，，，就先这样吧.
 */

@Mapper
public class MysqlJdbc {

    private final static String driverClassName = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) {
        new MysqlJdbc().getAllTables("movie");
    }

    public void getAllTables(String tableName) {
        String url = "jdbc:mysql://localhost:3306/integrate?serverTimezone=UTC";
        String mysqlUserName = "root";
        String password = "1234";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {

            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url, mysqlUserName, password);

            String sql = "show columns from "+tableName;
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            for(int i=1;i<=resultSet.getMetaData().getColumnCount();++i){
                System.out.println(resultSet.getMetaData().getColumnName(i));
            }
            while (resultSet.next()) {
                TableHead tableHead = new TableHead();
                tableHead.setField(resultSet.getString("Field"));
                tableHead.setType(resultSet.getString("Type"));
                tableHead.setNullable(resultSet.getString("Null"));
                tableHead.setKey(resultSet.getString("Key"));
                tableHead.setDefaultValue(resultSet.getString("Default"));
                tableHead.setExtra(resultSet.getString("Extra"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();  //必须关
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
