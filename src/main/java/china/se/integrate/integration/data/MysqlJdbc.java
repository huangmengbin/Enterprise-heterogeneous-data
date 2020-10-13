package china.se.integrate.integration.data;

import china.se.integrate.integration.entity.TableHead;
import com.sun.rowset.CachedRowSetImpl;
import org.apache.ibatis.annotations.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author hmb
 * 最好的方法是使用连接池，不要每次查询都connect,close,浪费时间
 * 但我们主要是为了集成，即灵活性要求高，各种表字段未知?
 * 公司内部使用，并发度低，所以，，，就先这样吧.
 */

@Mapper
public class MysqlJdbc {

    private final static String driverName = "com.mysql.cj.jdbc.Driver";


    public static void main(String[] args) throws Exception{
        try {
            testTransMysqlToOracle();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void testTransMysqlToOracle() throws SQLException{
        final MysqlJdbc mysql = new MysqlJdbc();
        final OracleJdbc oracle = new OracleJdbc();

        String mysqlUrl = "jdbc:mysql://localhost:3306/integrate?serverTimezone=UTC";
        String mysqlUsername = "root";
        String mysqlPassword = "1234";
        Connection mysqlConnection = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword);;

        String oracleUrl="jdbc:oracle:thin:@localhost:1521/orcl";	//test为数据库名称，1521为连接数据库的默认端口
        String oracleUser="c##scotter".toUpperCase();	//用户名
        String oraclePassword="1234";	//1234为密码
        Connection oracleConnection = DriverManager.getConnection(oracleUrl, oracleUser, oraclePassword);;

        List<TableHead> movie = mysql.getAllTables(mysqlConnection, "movie");
        movie.forEach(System.out::println);
        oracle.createTableByHead(oracleConnection, movie, "movieOracle");

        ResultSet resultSet = mysql.selectAllFromTable(mysqlConnection, "movie");
        oracle.insertAll(oracleConnection, "movieOracle", resultSet);

        mysqlConnection.close();
        oracleConnection.close();
        resultSet.close();

    }

    public List<TableHead> getAllTables(Connection connection, String tableName) {


        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<TableHead> result = new ArrayList<>();
        try {



            String sql = "show columns from "+tableName;
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            for(int i=1;i<=resultSet.getMetaData().getColumnCount();++i){
                System.out.println(resultSet.getMetaData().getColumnName(i));
            }
            while (resultSet.next()) {
                TableHead tableHead = new TableHead(TableHead.DatabaseType.mysql);
                tableHead.setField(resultSet.getString("Field"));
                tableHead.setTypeAndSizeAndPrecision(resultSet.getString("Type"));
                tableHead.setNullable(resultSet.getString("Null"));
                tableHead.setKey(resultSet.getString("Key"));
                tableHead.setDefaultValue(resultSet.getString("Default"));
                tableHead.setExtra(resultSet.getString("Extra"));
                result.add(tableHead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public void createTableByHead(Connection connection, List<TableHead> tableHeadList, String tableName){

        PreparedStatement preparedStatement = null;

        try {

            //定制sql命令
            StringBuilder sql = new StringBuilder("CREATE TABLE " + '`'+ tableName +'`' + " (" );

            for (Iterator<TableHead> iterator = tableHeadList.iterator(); iterator.hasNext(); ) {
                TableHead h = iterator.next();
                h.setTransfer(TableHead.DatabaseType.mysql);
                sql.append('`').append(h.getField()).append('`').append(' ');
                sql.append(h.getType()).append(' ');

                sql.append(h.getDefaultValue());
                sql.append(h.getNullable());



                if(h.getKey().equals("PRI")){
                    sql.append("primary key ");
                } else if(h.getKey().equals("UNI")){
                    sql.append("unique ");
                }



                if(iterator.hasNext()){
                    sql.append(" , ");
                } else {
                    sql.append(" ) ;");
                }
            }
            System.out.println(sql.toString());

            preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS `"+tableName+"`;");
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement!= null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public ResultSet selectAllFromTable(Connection connection, String tableName){
        ResultSet resultSet;
        CachedRowSetImpl trueResult = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from `" + tableName + "`;");
            resultSet = preparedStatement.executeQuery();
            trueResult = new CachedRowSetImpl();
            trueResult.populate(resultSet);
            preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return trueResult;
    }

    public void insertAll(Connection connection, String tableName, ResultSet resultSet){
        try {
            int total = resultSet.getMetaData().getColumnCount();
            StringBuilder sql = new StringBuilder("INSERT INTO `"+tableName+"` VALUES  ");
            while (resultSet.next()) {
                sql.append('(');
                for(int i = 1; i<= total; ++i){
                    sql.append("'").append(resultSet.getString(i).replaceAll("'","''")).append("'");
                    if(i < total){
                        sql.append(',');
                    }
                }
                sql.append(')').append(',');
            }
            sql.replace(sql.lastIndexOf(","), sql.length(), ";");

            System.out.println(sql.toString());

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
