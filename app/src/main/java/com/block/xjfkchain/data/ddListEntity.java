package com.block.xjfkchain.data;

import java.io.Serializable;

/**
 * Copyright (C) 2020, Relx
 * FilEarnListEntity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/10/22, muwenlei, Create file
 */
public class ddListEntity implements Serializable {

    public String symbol;
    public String total_income;
    public String avail;
    public String today_income;
    public String pledge_fil;

    public String getPledge_fil() {
        return pledge_fil;
    }

    public void setPledge_fil(String pledge_fil) {
        this.pledge_fil = pledge_fil;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getToday_income() {
        return today_income;
    }

    public void setToday_income(String today_income) {
        this.today_income = today_income;
    }
}