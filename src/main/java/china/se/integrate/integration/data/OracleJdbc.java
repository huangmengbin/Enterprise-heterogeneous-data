package china.se.integrate.integration.data;

import china.se.integrate.integration.entity.TableHead;
import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author hmb
 */
public class OracleJdbc {
    private final static String driverName="oracle.jdbc.driver.OracleDriver";

    public static void main(String[] args){
        try {
            testTransOracleToMysql();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void testTransOracleToMysql() throws SQLException{
        final MysqlJdbc mysql = new MysqlJdbc();
        final OracleJdbc oracle = new OracleJdbc();

        String oracleUrl="jdbc:oracle:thin:@localhost:1521/orcl";	//test为数据库名称，1521为连接数据库的默认端口
        String oracleUser="c##scotter".toUpperCase();	//用户名
        String oraclePassword="1234";	//1234为密码
        Connection oracleConnection = DriverManager.getConnection(oracleUrl, oracleUser, oraclePassword);;

        String mysqlUrl = "jdbc:mysql://localhost:3306/integrate?serverTimezone=UTC";
        String mysqlUsername = "root";
        String mysqlPassword = "1234";
        Connection mysqlConnection = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword);;


        List<TableHead> filmComment = oracle.getAllTables(oracleConnection, "FILMCOMMENT");
        filmComment.forEach(System.out::println);
        ResultSet resultSet = oracle.selectAllFromTable(oracleConnection, "FILMCOMMENT");

        mysql.createTableByHead(mysqlConnection, filmComment, "filmComment01");
        mysql.insertAll(mysqlConnection, "filmComment01", resultSet);

        mysqlConnection.close();
        oracleConnection.close();
        resultSet.close();
    }

    public List<TableHead> getAllTables(Connection connection, String tableName){
        //tableName区分大小写？？？还是必须要大写？？？？？
        tableName = tableName.toUpperCase();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //数据库连接对象

        ArrayList<TableHead> result = new ArrayList<>();
        try {


            //定制sql命令
            String sql = "select * from user_tab_columns t where t.table_name ='"   + tableName +  "' " ;
//            String sql = "select * from user_tab_columns t, user_constraints c, user_cons_columns u " +
//                    "where t.table_name ='"   + tableName +  "' "  +
//                    "and t.table_name = c.table_name " +
//                    "and u.table_name = t.table_name " +
//                    "and u.constraint_name=c.constraint_name " +
//                    "and u.column_name=t.column_name order by t.column_name" ;

            //创建该连接下的PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            for(int i=1;i<=resultSet.getMetaData().getColumnCount();++i){
                System.out.println(resultSet.getMetaData().getColumnName(i));
            }


            while (resultSet.next()){
                TableHead tableHead = new TableHead(TableHead.DatabaseType.oracle);
                tableHead.setField(resultSet.getString("COLUMN_NAME"));
                tableHead.setTypeAndSizeAndPrecision(resultSet.getString("DATA_TYPE")+
                        "$"+(resultSet.getString("DATA_PRECISION")==null?resultSet.getString("DATA_LENGTH"):resultSet.getString("DATA_PRECISION"))+
                        "#"+resultSet.getString("DATA_SCALE"));
                tableHead.setNullable(resultSet.getString("NULLABLE"));
                tableHead.setDefaultValue(resultSet.getString("DATA_DEFAULT"));
                tableHead.setExtra("");     //todo
                tableHead.setKey("");       //todo
                result.add(tableHead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(preparedStatement != null){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    public void createTableByHead(Connection connection, List<TableHead> tableHeadList, String tableName){
        tableName = tableName.toUpperCase();
        PreparedStatement preparedStatement = null;
        try {
            String username= connection.getSchema().toUpperCase();

            String before = "select * from user_tab_columns t where t.table_name ='"   + tableName +  "' " ;
            preparedStatement = connection.prepareStatement(before);
            if(preparedStatement.executeQuery().next()){
                String drop = "drop TABLE " +'"'+ username +'"'+'.'+'"'+ tableName +'"';
                connection.prepareStatement(drop).executeUpdate();
            }

            //定制sql命令
            StringBuilder sql = new StringBuilder("CREATE TABLE " +'"'+ username +'"'+'.'+'"'+ tableName +'"' + " (" );

            for (Iterator<TableHead> iterator = tableHeadList.iterator(); iterator.hasNext(); ) {
                TableHead h = iterator.next();
                h.setTransfer(TableHead.DatabaseType.oracle);
                sql.append('"').append(h.getField()).append('"').append(' ')
                        .append(h.getType()).append(' ');

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
                    sql.append(" ) ");  // 结尾不允许有;
                }
            }
            System.out.println(sql.toString());

            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                if(preparedStatement != null){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public ResultSet selectAllFromTable(Connection connection, String tableName){
        tableName = tableName.toUpperCase();
        ResultSet resultSet;
        CachedRowSetImpl trueResult = null;
        try {
            String username= connection.getSchema().toUpperCase();

            PreparedStatement preparedStatement = connection.prepareStatement("select * from "  +'"'+ username +'"'+'.'+'"'+ tableName +'"');
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
            tableName = tableName.toUpperCase();
            int total = resultSet.getMetaData().getColumnCount();
            String username= connection.getSchema().toUpperCase();
            while (resultSet.next()) {
                StringBuilder sql = new StringBuilder("insert into " + username + '.' + tableName + " values ( ");

                for(int i = 1; i<= total; ++i) {
                    sql.append("'").append(resultSet.getString(i).replaceAll("'","''")).append("'");
                    if(i < total){
                        sql.append(',');
                    }
                }

                sql.append(")");
                System.out.println(sql.toString());
                PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
