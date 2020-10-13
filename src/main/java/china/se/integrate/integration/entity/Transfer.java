package china.se.integrate.integration.entity;
/**
 * @author hmb
 */
abstract class Transfer {

    abstract String transToField(String field);
    abstract String transToType(String s);
    abstract Integer transToSize(String s);               //数据长度
    abstract Integer transToPrecision(String s);          //小数点后xx位
    abstract Boolean transToNullable(String nullable);
    abstract String transToKey(String key);
    abstract String transToDefaultValue(String defaultValue);
    String transExtra(String extra){
        if ( extra==null ) {
            return "";
        } else {
            return extra;
        }
    }

    abstract String getField(String field);
    abstract String getType(String type, Integer size, Integer precision);
    abstract String getNullable(Boolean nullable);
    abstract String getKey(String key);
    abstract String getDefaultValue(String defaultValue);
    abstract String getExtra(String extra);
}