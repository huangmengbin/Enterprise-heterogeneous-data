package china.se.integrate.integration.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author hmb
 */
class MysqlTransfer extends Transfer{

    //private final static int FLOAT_SIZE = 0, DOUBLE_SIZE = 0, DECIMAL_SIZE = 0;
    //private final static int FLOAT_PRECISION = 0, DOUBLE_PRECISION = 0, DECIMAL_PRECISION = 0;
    private final static int TINY_INT_SIZE = 3, SMALL_INT_SIZE = 5, MEDIUM_INT_SIZE = 7, INT_SIZE = 10, BIG_INT_SIZE = 20;
    private static final BiMap<Integer, String> sizeToTypeMap = HashBiMap.create();
    static {
        sizeToTypeMap.put(TINY_INT_SIZE, "tinyint");
        sizeToTypeMap.put(SMALL_INT_SIZE, "smallint");
        sizeToTypeMap.put(MEDIUM_INT_SIZE, "mediumint");
        sizeToTypeMap.put(INT_SIZE, "int");
        sizeToTypeMap.put(BIG_INT_SIZE, "bigint");
    }

    @Override
    String transToField(String field) {
        return field;
    }

    @Override
    String transToType(String s) {
        if(s.contains("(")){
            s = s.substring(0, s.indexOf('('));
        }
        if(s.contains("float")|s.contains("double")|s.contains("dec")){
            s = "decimal";
        } else if(s.contains("int")) {
            s = "integer";
        }
        return s;
    }

    @Override
    Integer transToSize(String s) {
        if(s.contains("int")){
            return sizeToTypeMap.inverse().get(s.substring(0,s.indexOf('(')));
        } else if(s.contains("varchar")){
            return Integer.parseInt(s.substring( s.indexOf('(')+1 , s.indexOf(')') ));
        } else if(s.contains("float")|s.contains("double")|s.contains("dec")) {
            return 38;//todo
        }
        return null;
    }

    @Override
    Integer transToPrecision(String s) {
        if(s.contains("int")){
            return 0;
        } else if(s.contains("float")|s.contains("double")|s.contains("dec")) {
            return 20;//todo
        }
        return null;
    }

    @Override
    Boolean transToNullable(String nullable) {
        return nullable.equals("YES");
    }

    @Override
    String transToKey(String key) {
        return key;
    }

    @Override
    String transToDefaultValue(String defaultValue) {
        return defaultValue;
    }




    /**
     *
     * getters
     *
     * */

    @Override
    String getField(String field) {
        return field;
    }

    @Override
    String getType(String type, Integer size, Integer precision) {
        String result = type;
        switch (type) {
            case "integer":
                result = sizeToTypeMap.get(size);
                if(result == null){
                    result="number("+size+")";
                }
                break;
            case "decimal":
                result = "decimal(" + size + ", " + precision + ")";
                break;
            case "varchar":
                result = "varchar(" + size + ")";
                break;
        }
        return result;
    }

    @Override
    String getNullable(Boolean nullable) {
        return nullable? " null ":" not null ";
    }

    @Override
    String getKey(String key) {
        return key;
    }

    @Override
    String getDefaultValue(String defaultValue) {
        if(defaultValue==null || defaultValue.equals("")) {
            return " ";
        } else {
            return "default "+ defaultValue +" ";
        }
    }

    @Override
    String getExtra(String extra) {
        return extra;
    }
}
