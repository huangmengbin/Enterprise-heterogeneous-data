package china.se.integrate.integration.entity;

/**
 * @author hmb
 */
public class TableHead {
    private String field, type, nullable, key, defaultValue, extra;

    @Override
    public String toString() {
        return "TableHead{" +
                "field='" + field + '\'' +
                ", type='" + type + '\'' +
                ", nullable='" + nullable + '\'' +
                ", key='" + key + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


}
