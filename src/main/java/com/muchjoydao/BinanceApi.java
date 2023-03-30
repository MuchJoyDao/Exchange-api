package com.muchjoydao;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.WithdrawResult;

public class BinanceApi {
    public static void main(String[] args) {
        String apiKey = PropertiesUtils.getValue("binance.apiKey");
        String secretKey = PropertiesUtils.getValue("binance.secretKey");
        //必须
        String coin = PropertiesUtils.getValue("binance.coin");
        String address = PropertiesUtils.getValue("binance.address");
        //非必须 提币网络
        String network = PropertiesUtils.getValue("binance.network");
        //必填 数量
        String  amount =  PropertiesUtils.getValue("binance.amount");
        if (StrUtil.hasBlank(apiKey, secretKey, coin, address, amount)) {
            throw new RuntimeException("币安配置信息错误");
        }
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
        // Withdraw 接口现有的必须参数 如果不够改项目源码，如本接口加了网络network
        //String asset, String address, String amount, String name, String addressTag， String network
        WithdrawResult result=client.withdraw(coin, address, amount, null, null,network);
        Console.log("{} - 结果{}->{}", DateUtil.date(),result.isSuccess(),result.getMsg());

    }
}
