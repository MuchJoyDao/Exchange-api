package com.muchjoydao;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.WithdrawResult;
import com.muchjoydao.model.KeyValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BinanceApi {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = PropertiesUtils.getValue("binance.apiKey");
        String secretKey = PropertiesUtils.getValue("binance.secretKey");
        //必须
        String coin = PropertiesUtils.getValue("binance.coin");
        //非必须 提币网络
        String network = PropertiesUtils.getValue("binance.network");
        //必填 数量
        String purchasingBaseAmount = PropertiesUtils.getValue("binance.baseAmount");
        String purchasingMaxAmount = PropertiesUtils.getValue("binance.MaxAmount");
        //间隔时间
        String intervalTime = PropertiesUtils.getValue("binance.intervalBaseTime");
        String intervalRandomTime = PropertiesUtils.getValue("binance.intervalRandomTime");

        //读取地址
        String[] addr;
        String fileName = PropertiesUtils.getValue("binance.address");
        if (fileName.contains(".xlsx")) {
            List<KeyValue> list = EasyExcel.read(fileName).head(KeyValue.class).sheet().doReadSync();
            addr = list.stream().map(KeyValue::getAddress).filter(x -> x != null).collect(Collectors.toList()).stream().toArray(String[]::new);
        } else {
            addr = PropertiesUtils.getArray("binance.address", ",");
        }

        if (StrUtil.hasBlank(apiKey, secretKey, coin, purchasingBaseAmount, purchasingMaxAmount, intervalRandomTime, intervalTime, addr[0])) {
            throw new RuntimeException("币安配置信息错误");
        }
        //同步
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
        // Withdraw 接口现有的必须参数 如果不够改项目源码，如本接口加了网络network
        Random rand = new Random();
        for (String address : addr) {
            int time = rand.nextInt(Integer.parseInt(intervalRandomTime)) + Integer.parseInt(intervalTime);
            BigDecimal amount= makeRandom(Float.valueOf(purchasingMaxAmount),Float.valueOf(purchasingBaseAmount),3);
            Console.log("地址 {} 转帐金额 {}", address, amount.toString());
            WithdrawResult result = client.withdraw(coin, address, amount.toString(), null, null, network);
            Console.log("当前时间 {} 休眠时间 {}", DateUtil.date(), time);
            Thread.sleep(time);
        }

    }

    private static BigDecimal makeRandom(float max, float min, int scale) {
        BigDecimal cha = new BigDecimal(Math.random() * (max - min) + min);
        return cha.setScale(scale, BigDecimal.ROUND_HALF_UP);//保留 scale 位小数，并四舍五入
    }
}
