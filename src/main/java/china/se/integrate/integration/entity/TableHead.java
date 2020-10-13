package china.se.integrate.integration.entity;


/**
 * @author hmb
 */
public class TableHead {
    private String
            field,
            type, // varchar, integer, decimal
            key,
            defaultValue,
            extra;
    //关于type，integer指通用的小数, decimal指通用的小数。具体的数长、精确度还得存放在size、precision中。
    private Integer size, precision;
    private Boolean nullable; //Y, N

    private Transfer transfer;
    private static final MysqlTransfer  mysqlTransfer = new MysqlTransfer();
    private static final OracleTransfer oracleTransfer = new OracleTransfer();

    public enum DatabaseType{
        oracle,
        mysql
    }

    /* public TableHead(){} */
    public final boolean isNumber(){
        return type.equals("integer") || type.equals("decimal");
    }

    public TableHead(DatabaseType type){
        setTransfer(type);          //这里的处置反了，应该让transfer持有data，哪怕影响时间效率
    }

    @Override
    public String toString() {
        return "TableHead{" +
                "field='" + getField() + '\'' +
                ", type='" + getType() + '\'' +
                ", nullable='" + getNullable() + '\'' +
                ", key='" + getKey() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
                ", extra='" + getExtra() + '\'' +
                ", size=" + getSize() +
                ", precision=" + getPrecision() +
                '}';
    }

    public void setTransfer(DatabaseType type){
        switch (type){
            case mysql:  transfer=mysqlTransfer;  break;
            case oracle: transfer=oracleTransfer; break;
            default: throw new RuntimeException("什么数据库啊?");
        }
    }


    public String getField() {
        return transfer.getField(field);
    }

    public void setField(String field) {
        this.field = transfer.transToField(field);
    }

    public String getType() {
        return transfer.getType(type, size, precision);
    }

    public Integer getSize() {
        return size;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setTypeAndSizeAndPrecision(String typeAndSizeAndPrecision) {
        typeAndSizeAndPrecision = typeAndSizeAndPrecision.toLowerCase();
        this.type = transfer.transToType(typeAndSizeAndPrecision);
        this.size = transfer.transToSize(typeAndSizeAndPrecision);
        this.precision = transfer.transToPrecision(typeAndSizeAndPrecision);
    }

    public String getNullable() {
        return transfer.getNullable(nullable);
    }

    public void setNullable(String nullable) {
        this.nullable = transfer.transToNullable(nullable);
    }

    public String getKey() {
        return transfer.getKey(key);
    }

    public void setKey(String key) {
        this.key = transfer.transToKey(key);
    }

    public String getDefaultValue() {
        return transfer.getDefaultValue(defaultValue);
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = transfer.transToDefaultValue(defaultValue);
    }

    public String getExtra() {
        return transfer.getExtra(extra);
    }

    public void setExtra(String extra) {
        this.extra = transfer.transExtra(extra);
    }


}
