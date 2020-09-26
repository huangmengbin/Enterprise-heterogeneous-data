package china.se.integrate.integration.data;

import china.se.integrate.integration.entity.TableHead;

import java.sql.*;
import java.util.ArrayList;


/**
 * @author hmb
 */
public class OracleJdbc {
    private final static String driverName="oracle.jdbc.driver.OracleDriver";

    public static void main(String[] args) {
            new OracleJdbc().SelectUser("FILMCOMMENT");
    }
    public void SelectUser(String tableName){
        //tableName区分大小写？？？还是必须要大写？？？？？


        //设定数据库驱动，数据库连接地址、端口、名称，用户名，密码

        String url="jdbc:oracle:thin:@localhost:1521/orcl";	//test为数据库名称，1521为连接数据库的默认端口
        String user="c##scotter";	//用户名
        String password="1234";	//1234为密码
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //数据库连接对象
        Connection connection = null;

        try {
            //反射Oracle数据库驱动程序类
            Class.forName(driverName);

            //获取数据库连接
            connection = DriverManager.getConnection(url, user, password);

            //定制sql命令
            String sql = "select column_name,data_type,nullable,data_default from user_tab_columns where table_name ='"+tableName+"'";

            //创建该连接下的PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

//            for(int i=1;i<=resultSet.getMetaData().getColumnCount();++i){
//                System.out.println(resultSet.getMetaData().getColumnName(i));
//            }

            ArrayList<TableHead> arrayList = new ArrayList<>();
            while (resultSet.next()){
                TableHead tableHead = new TableHead();
                tableHead.setField(resultSet.getString(1));
                tableHead.setType(resultSet.getString(2));
                tableHead.setNullable(resultSet.getString(3));
                tableHead.setDefaultValue(resultSet.getString(4));
                arrayList.add(tableHead);
                //tableHead.setKey(resultSet.getString("Key"));
                //tableHead.setExtra(resultSet.getString("Extra"));
            }

            arrayList.forEach(System.out::println);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
