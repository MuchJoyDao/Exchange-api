package com.muchjoydao.model;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class KeyValue {
    @ExcelProperty("地址")
    private String address;
    @ExcelProperty("私钥")
    private String key;
}
