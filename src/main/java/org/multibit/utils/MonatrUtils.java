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

public class MonatrUtils {

    public static class MonatrTicker {
        public String currency;
        // public BigDecimal last;
        public BigDecimal bid;
        public BigDecimal ask;
    }

    private static Logger log = LoggerFactory.getLogger(MonatrUtils.class);

    private static final URL MONATR_API_URL;
    private static final URL BITPAY_API_URL;
    static
    {
        try
        {
            MONATR_API_URL = new URL("http://api.monatr.jp/ticker?market=BTC_MONA");
            BITPAY_API_URL = new URL("https://bitpay.com/api/rates");
        }
        catch (final MalformedURLException x)
        {
            throw new RuntimeException(x); // cannot happen
        }
    }

    public static MonatrTicker requestMonaBitpayTicker(String currencyCode) {
        HttpURLConnection connection = null;
        HttpURLConnection connBitPay = null;
        Reader reader = null;
        Reader readerBitPay = null;

        try
        {
            connection = (HttpURLConnection) MONATR_API_URL.openConnection();
            connection.setConnectTimeout(Constants.HTTP_TIMEOUT_MS);
            connection.setReadTimeout(Constants.HTTP_TIMEOUT_MS);
            connection.connect();

            final int responseCode = connection.getResponseCode();

            connBitPay = (HttpURLConnection) BITPAY_API_URL.openConnection();
            connBitPay.setConnectTimeout(Constants.HTTP_TIMEOUT_MS);
            connBitPay.setReadTimeout(Constants.HTTP_TIMEOUT_MS);
            connBitPay.connect();
            final int respCodeBitPay = connBitPay.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK
                && respCodeBitPay == HttpURLConnection.HTTP_OK
                )
            {
                reader = new InputStreamReader(new BufferedInputStream(connection.getInputStream(), 1024), Constants.UTF_8);
                final StringBuilder content = new StringBuilder();
                Io.copy(reader, content);

                readerBitPay = new InputStreamReader(new BufferedInputStream(connBitPay.getInputStream(), 1024), Constants.UTF_8);
                final StringBuilder contentBitPay = new StringBuilder();
                Io.copy(readerBitPay, contentBitPay);

                try
                {
                    JSONObject ticker = (JSONObject)JSONValue.parse(content.toString());
                    BigDecimal bidMona =  new BigDecimal(ticker.get("current_bid").toString());
                    BigDecimal askMona =  new BigDecimal(ticker.get("current_ask").toString());

                    JSONArray bitpay = (JSONArray)JSONValue.parse(contentBitPay.toString());
                    BigDecimal curRate = null;
                    for(Object cur:bitpay){
                        JSONObject curObj = (JSONObject)cur;
                        if(curObj.get("code").toString().equals( currencyCode )){
                            curRate = new BigDecimal(curObj.get("rate").toString());
                            break;
                        }
                    }
                    if( curRate != null){
                        MonatrUtils.MonatrTicker ret = new MonatrUtils.MonatrTicker();
                        ret.currency = currencyCode;
                        ret.ask      = curRate.divide(bidMona , 8 , 
                                                      java.math.RoundingMode.DOWN);
                        ret.bid      = curRate.divide(askMona , 8 , 
                                                      java.math.RoundingMode.DOWN);
                        return ret;
                    } else {
                        return null;
                    }
                } catch (NumberFormatException e)
                {
                    log.debug("Hm, looks like monatr.jp/BitPay changed their API...");
                    return null;
                }

            }
            else
            {
                log.debug("http status " + responseCode + " when fetching " + MONATR_API_URL);
            }
        }
        catch (final Exception x)
        {
            log.debug("problem reading exchange rates", x);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (final IOException x)
                {
                    // swallow
                }
            }

            if (connection != null)
                connection.disconnect();
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
