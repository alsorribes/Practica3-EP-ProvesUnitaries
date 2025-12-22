package data;

/**
 * TEMPORARY -
 * Universal Product Code for medicines.
 */
public final class ProductID {
    private final String code;

    public ProductID(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductID productID = (ProductID) o;
        return code.equals(productID.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ProductID{" + "code='" + code + '\'' + '}';
    }
}