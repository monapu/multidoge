package org.multibit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.*;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.zip.GZIPInputStream;

public class MonaUtils {

    public static class MonaTicker {
        public String currency;
        public BigDecimal last = null;
        public BigDecimal bid = null;
        public BigDecimal ask = null;
    }

    private static class RemoteData {
        private URL apiUrl = null;
        private String dataString = null;
        private Date expireAt = null;
        private static final long CACHE_TIME = 60 * 1000;
        
        public RemoteData(URL url){
            apiUrl = url;
        }
        
        public RemoteData(String url){
            try{
                apiUrl = new URL( url );
            } catch (final MalformedURLException x){
                //
            }
        }
        
        public synchronized String get(){
            Date now = new Date();
            if( apiUrl != null && 
                (dataString == null || expireAt == null || expireAt.before(now)) ){
                
                HttpURLConnection connection = null;
                Reader reader = null;
                try {
                    connection = (HttpURLConnection) apiUrl.openConnection();
                    connection.setConnectTimeout(Constants.HTTP_TIMEOUT_MS);
                    connection.setReadTimeout(Constants.HTTP_TIMEOUT_MS);
                    connection.setInstanceFollowRedirects(true);
                    connection.connect();
                    final int responseCode = connection.getResponseCode();
                    
                    if (responseCode == HttpURLConnection.HTTP_OK){
                        String encoding = connection.getContentEncoding();
                        if(encoding != null && encoding.contains("gzip")){
                            reader = new InputStreamReader(new BufferedInputStream(new GZIPInputStream(connection.getInputStream()) , 1024) , Constants.UTF_8);
                        } else {
                            reader = new InputStreamReader(new BufferedInputStream(connection.getInputStream(), 1024), Constants.UTF_8);
                        }
                        final StringBuilder content = new StringBuilder();
                        Io.copy(reader, content);
                        
                        expireAt = new Date( now.getTime() + CACHE_TIME );
                        dataString = content.toString();
                    } else {
                        log.debug("http status " + responseCode + " when fetching " + 
                                  apiUrl);
                        dataString = null;
                    }
                } catch (final Exception x){
                    log.debug("problem reading exchange rates", x);
                    dataString = null;
                } finally {
                    if( reader != null)
                        try {
                            reader.close();
                        } catch( final IOException x) {
                            
                        }
                    
                    if( connection != null)
                        connection.disconnect();
                }
            }
            
            return dataString;
        } // get

    } // class RemoteData

    private static Logger log = LoggerFactory.getLogger(MonaUtils.class);

    private static RemoteData monatrApiData;
    private static RemoteData bitpayApiData;
    private static RemoteData allcoinDepthData;
    private static RemoteData allcoinTradeData;

    static {
        monatrApiData = new RemoteData( "https://api.monatr.jp/ticker?market=BTC_MONA");
        bitpayApiData = new RemoteData( "https://bitpay.com/api/rates");
        allcoinDepthData = new RemoteData( "https://www.allcoin.com/api1/depth/mona_btc");
        allcoinTradeData = new RemoteData( "https://www.allcoin.com/api1/trade/mona_btc");
    }

    private BigDecimal getBitpayRate(String currencyCode){
        String bitpayJsonStr = bitpayApiData.get();
        BigDecimal curRate = null;
        if( bitpayJsonStr != null){
            try{
                JSONArray bitpay = (JSONArray)JSONValue.parse(bitpayJsonStr);
                for(Object cur:bitpay){
                    JSONObject curObj = (JSONObject)cur;
                    if(curObj.get("code").toString().equals( currencyCode )){
                        curRate = new BigDecimal(curObj.get("rate").toString());
                        break;
                    }
                }
                return curRate;
            } catch (NumberFormatException e){
                log.debug("Hm, looks like bitpay changed their API...");
                return null;
            } catch (NullPointerException e){
                log.debug("Hm, looks like bitpay changed their API...");
                return null;
            }
        }
        return null;
    }

    public MonaTicker requestMonatrBitpayTicker(String currencyCode) {
        
        String tickerJsonStr = monatrApiData.get();
        BigDecimal curRate = getBitpayRate(currencyCode);

        if( tickerJsonStr != null && curRate != null){
            try {
                JSONObject ticker = (JSONObject)JSONValue.parse(tickerJsonStr);
                BigDecimal bidMona =  new BigDecimal(ticker.get("current_bid").toString());
                BigDecimal askMona =  new BigDecimal(ticker.get("current_ask").toString());

                MonaTicker ret = new MonaTicker();
                ret.currency = currencyCode;
                ret.ask      = curRate.divide(bidMona , 8 , 
                                              java.math.RoundingMode.HALF_UP);
                ret.bid      = curRate.divide(askMona , 8 , 
                                              java.math.RoundingMode.HALF_UP);
                return ret;
            } catch (NumberFormatException e){
                log.debug("Hm, looks like monatr.jp changed their API...");
                return null;
            } catch (NullPointerException e) {
                log.debug("Hm, looks like monatr.jp changed their API...");
                return null;
            }
        
        }
        return null;
    }

    public MonaTicker requestAllcoinBitpayTicker(String currencyCode) {
        String depthStr = allcoinDepthData.get();
        String tradeStr = allcoinTradeData.get();
        BigDecimal curRate = getBitpayRate(currencyCode);

        if( depthStr != null && tradeStr != null && curRate != null){
            MonaTicker ret = new MonaTicker();
            try {
                JSONObject depth = (JSONObject)JSONValue.parse( depthStr );
                JSONObject data  = (JSONObject)depth.get("data");
                JSONObject sellOrders = (JSONObject)data.get("sell");
                JSONObject buyOrders  = (JSONObject)data.get("buy");
                String currentBid = null;
                String currentAsk = null;
                for( Object k : sellOrders.keySet() ){
                    String ks = k.toString();
                    if( currentAsk == null || ks.compareTo( currentAsk ) < 0)
                        currentAsk = ks;
                }
                for( Object k : buyOrders.keySet() ){
                    String ks = k.toString();
                    if( currentBid == null || ks.compareTo( currentBid ) > 0)
                        currentBid = ks;
                }

                JSONObject tradeHist = (JSONObject)JSONValue.parse( tradeStr);
                JSONArray  trades    = (JSONArray)tradeHist.get("data");
                JSONObject lastTrade = (JSONObject)trades.get(0);
                String last = lastTrade.get("price").toString();

                ret.currency = currencyCode;
                ret.ask = 
                    (new BigDecimal( currentAsk )).multiply(curRate);
                ret.bid = 
                    (new BigDecimal( currentBid )).multiply(curRate);
                ret.last = 
                    (new BigDecimal( last )).multiply(curRate);
                
                return ret;
                
            } catch (NumberFormatException e)
                {
                    log.debug("Hm, looks like Allcoin.com/BitPay changed their API...");
                    return null;
            } catch (NullPointerException e)
                {
                    log.debug("Hm, looks like Allcoin.com/BitPay changed their API...");
                    return null;
                }
        }
        return null;
    }

    private static final String[] CURRENCIES = { "USD","EUR","GBP","JPY","CAD","AUD","CNY","CHF","SEK","NZD","KRW","AED","AFN","ALL","AMD","ANG","AOA","ARS","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTN","BWP","BYR","BZD","CDF","CLF","CLP","COP","CRC","CVE","CZK","DJF","DKK","DOP","DZD","EEK","EGP","ETB","FJD","FKP","GEL","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","INR","IQD","ISK","JEP","JMD","JOD","KES","KGS","KHR","KMF","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","LTL","LVL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SGD","SHP","SLL","SOS","SRD","STD","SVC","SYP","SZL","THB","TJS","TMT","TND","TOP","TRY","TTD","TWD","TZS","UAH","UGX","UYU","UZS","VEF","VND","VUV","WST","XAF","XAG","XAU","XCD","XOF","XPF","YER","ZAR","ZMW","ZWL"};
    public static ArrayList<String> getAvailableCurrencies() {
        ArrayList<String> ret = new ArrayList<String>();
        for(int i = 0; i < CURRENCIES.length; i++){
            ret.add( CURRENCIES[i] );
        }
        return ret;
    }

}
