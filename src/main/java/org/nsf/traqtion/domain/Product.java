package org.nsf.traqtion.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "secondary_title")
    private String secondaryTitle;

    @Column(name = "product_indentifier")
    private String productIndentifier;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "u_pc")
    private String uPC;

    @OneToOne
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToOne
    private PrimarySpec primarySpec;

    @OneToOne
    private ProductStatus productStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public String getProductIndentifier() {
        return productIndentifier;
    }

    public void setProductIndentifier(String productIndentifier) {
        this.productIndentifier = productIndentifier;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getuPC() {
        return uPC;
    }

    public void setuPC(String uPC) {
        this.uPC = uPC;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public PrimarySpec getPrimarySpec() {
        return primarySpec;
    }

    public void setPrimarySpec(PrimarySpec primarySpec) {
        this.primarySpec = primarySpec;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", secondaryTitle='" + secondaryTitle + "'" +
            ", productIndentifier='" + productIndentifier + "'" +
            ", productCode='" + productCode + "'" +
            ", uPC='" + uPC + "'" +
            '}';
    }
}
