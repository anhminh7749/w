package com.watch.shopwatchonline.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private String startday;
    private String startmonth;
    private String startyear;
    private String endday;
    private String endmonth;
    private String endyear;
    private String value;
    
}
