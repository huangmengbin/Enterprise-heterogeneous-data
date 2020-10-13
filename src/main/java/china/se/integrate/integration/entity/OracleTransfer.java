package china.se.integrate.integration.entity;
/**
 * @author hmb
 */

class OracleTransfer extends Transfer{

    @Override
    String transToField(String field) {
        return field;
    }

    @Override
    String transToType(String s) {
        Integer precision = transToPrecision(s);
        s = s.substring(0, s.indexOf('$'));
        if(s.equals("varchar2")){
            return "varchar";
        }
        else if(precision==null) {
            return s;
        } else if(precision==0){
            return "integer";
        } else if(precision>0){
            return "decimal";
        }
        return s;
    }

    @Override
    Integer transToSize(String s) {
        s = s.substring(s.indexOf('$')+1, s.indexOf('#'));
        if (!s.equals("") && !s.equals("null")) {
            return Integer.parseInt(s);
        }
        return null;
    }

    @Override
    Integer transToPrecision(String s) {
        s = s.substring(s.indexOf('#')+1);
        if (!s.equals("") && !s.equals("null")) {
            return Integer.parseInt(s);
        }
        return null;
    }

    @Override
    Boolean transToNullable(String nullable) {
        return nullable.equals("Y");
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
        switch (type){
            case "integer":
            case "decimal":
                result = "number("+size+", "+precision+")";
                break;
            case "varchar":
                if(size<4001) {
                    result = "varchar2(" + size + ")";
                } else {
                    throw new RuntimeException(size.toString());
                }

        }
        return result;
    }

    @Override
    String getNullable(Boolean nullable) {
        return nullable? "null ":"not null ";
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