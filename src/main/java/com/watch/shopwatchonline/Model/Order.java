package com.watch.shopwatchonline.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    
     @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;
     
   
      @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date CompleteDate; //Ngày hoàn thành

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmationDate; // ngày xác nhận

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancellationDate;  //ngày hủy bỏ

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date DeliveringDate; //  Ngày giao hàng

    @Column
    private Short Status;

    @ManyToOne
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "discountCodeId")
    private DiscountCode discountCode;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

}
