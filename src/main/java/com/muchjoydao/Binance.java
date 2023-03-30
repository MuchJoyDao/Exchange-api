package com.muchjoydao;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.math.BigDecimal;
import java.util.Date;

public class Binance {
    public static void main(String[] args) {
        String apiKey = PropertiesUtils.getValue("binance.apiKey");
        String secretKey = PropertiesUtils.getValue("binance.secretKey");
        String startTime = PropertiesUtils.getValue("binance.startTime");
        String limitPrice = PropertiesUtils.getValue("binance.limitPrice");
        String originalAmount = PropertiesUtils.getValue("binance.originalAmount");
        String currencyBase = PropertiesUtils.getValue("binance.currencyBase");
        String threadNum = PropertiesUtils.getValue("common.threadNum");
        String tryNum = PropertiesUtils.getValue("common.tryNum");
        if (StrUtil.hasBlank(apiKey, secretKey, startTime, limitPrice, originalAmount, currencyBase)) {
            throw new RuntimeException("币安配置信息错误");
        }
        Currency counter = Currency.BUSD;
        Console.log("定时启动->[{}],买入->[{}],买入价->[{}],买入数量->[{}],需要{}数量->[{}]", startTime, currencyBase + "/" + counter.getCurrencyCode(), limitPrice, originalAmount, counter, NumberUtil.mul(limitPrice, originalAmount));
        do {

        } while (System.currentTimeMillis() < DateUtil.parseDateTime(startTime).getTime());


        int for1 = 8;
        int for2 = 5;
        if (StrUtil.isNotBlank(threadNum)) {
            for1 = Integer.parseInt(threadNum);
        }
        if (StrUtil.isNotBlank(tryNum)) {
            for2 = Integer.parseInt(tryNum);
        }


        for (int k = 0; k < for1; k++) {
            Console.log("开始!!!!!!!!!!!!");
            int finalFor = for2;
            new Thread(() -> {
                for (int i = 0; i < finalFor; i++) {
                    try {
                        ExchangeSpecification exSpec = new BinanceExchange().getDefaultExchangeSpecification();
                        exSpec.setApiKey(apiKey);
                        exSpec.setSecretKey(secretKey);
                        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exSpec);
                        Currency currency = Currency.getInstance(currencyBase);
                        CurrencyPair pair = new CurrencyPair(currency, counter);
                        String id = IdUtil.getSnowflake(1, 2).nextIdStr();
                        LimitOrder limitOrder = new LimitOrder(Order.OrderType.BID, new BigDecimal(originalAmount), pair, id, new Date(), new BigDecimal(limitPrice));
                        String res = exchange.getTradeService().placeLimitOrder(limitOrder);
                        Console.log("{} - {}号打工人第{}次请求,挂单成功!!!!!!,返回结果->{}",DateUtil.date(),Thread.currentThread().getId(), i + 1, res);
                    } catch (Exception e) {
                        Console.log("{} - {}号打工人第{}次请求,异常->{}", DateUtil.date(),Thread.currentThread().getId(),i + 1, e.getMessage());
                    }
                }
            }).start();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }

    }
}
