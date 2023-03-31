package com.muchjoydao;

import cn.hutool.core.util.StrUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.currency.Currency;

import java.io.IOException;
import java.math.BigDecimal;

public class BinanceWithdrawal {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = PropertiesUtils.getValue("binance.apiKey");
        String secretKey = PropertiesUtils.getValue("binance.secretKey");
        //必须
        String coin = PropertiesUtils.getValue("binance.coin");
        String[] addr = PropertiesUtils.getArray("binance.address",",");
        //必填 数量
        String  amount =  PropertiesUtils.getValue("binance.amount");
        if (StrUtil.hasBlank(apiKey, secretKey, coin, addr[0], amount)) {
            throw new RuntimeException("币安配置信息错误");
        }
        //Currency currency, BigDecimal amount, String address
        ExchangeSpecification exSpec = new BinanceExchange().getDefaultExchangeSpecification();
        exSpec.setApiKey(apiKey);
        exSpec.setSecretKey(secretKey);
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exSpec);
        //构造参数
        Currency currency = Currency.getInstance(coin);
        for (String address:addr){
            exchange.getAccountService().withdrawFunds(currency, new BigDecimal(amount),address);
            Thread.sleep(200000);
        }
    }
}
