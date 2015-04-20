package org.multibit.utils;

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.params.TestNet3Params;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author Andreas Schildbach
 */
public class Constants
{
    // public static final boolean TEST = R.class.getPackage().getName().contains("_test");

    // public static final NetworkParameters NETWORK_PARAMETERS = TEST ? TestNet3Params.get() : MainNetParams.get();
   //  private static final String FILENAME_NETWORK_SUFFIX = NETWORK_PARAMETERS.getId().equals(NetworkParameters.ID_MAINNET) ? "" : "-testnet";

   // public static final String WALLET_FILENAME = "wallet" + FILENAME_NETWORK_SUFFIX;

    // public static final String WALLET_FILENAME_PROTOBUF = "wallet-protobuf" + FILENAME_NETWORK_SUFFIX;

    /*
    public static final String WALLET_KEY_BACKUP_BASE58 = "key-backup-base58" + FILENAME_NETWORK_SUFFIX;

    // public static final File EXTERNAL_WALLET_BACKUP_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    public static final String EXTERNAL_WALLET_KEY_BACKUP = "dogecoin-wallet-keys" + FILENAME_NETWORK_SUFFIX;

    public static final String BLOCKCHAIN_FILENAME = "blockchain" + FILENAME_NETWORK_SUFFIX;

    public static final String CHECKPOINTS_FILENAME = "checkpoints" + FILENAME_NETWORK_SUFFIX;
*/
    private static final String BLOCKEXPLORER_BASE_URL_PROD = "http://abe.monash.pw/";
    private static final String BLOCKEXPLORER_BASE_URL_TEST = "https://abe.monash.pw/testnet/"; //DOGE: Not yet...
    // public static final String BLOCKEXPLORER_BASE_URL = NETWORK_PARAMETERS.getId().equals(NetworkParameters.ID_MAINNET) ? BLOCKEXPLORER_BASE_URL_PROD
           // : BLOCKEXPLORER_BASE_URL_TEST;

    public static final String MIMETYPE_TRANSACTION = "application/x-monatx";

    public static final int MAX_NUM_CONFIRMATIONS = 3;
    public static final String USER_AGENT = "Dogecoin Wallet";
    public static final String DEFAULT_EXCHANGE_CURRENCY = "USD";
    public static final int WALLET_OPERATION_STACK_SIZE = 256 * 1024;
    // public static final long BLOCKCHAIN_STATE_BROADCAST_THROTTLE_MS = DateUtils.SECOND_IN_MILLIS;
    // public static final long BLOCKCHAIN_UPTODATE_THRESHOLD_MS = DateUtils.HOUR_IN_MILLIS;

    public static final String CURRENCY_CODE_BTC = "MONA";
    public static final String CURRENCY_CODE_MBTC = "mMONA";
    public static final char CHAR_HAIR_SPACE = '\u200a';
    public static final char CHAR_THIN_SPACE = '\u2009';
    public static final char CHAR_ALMOST_EQUAL_TO = '\u2248';
    public static final String CURRENCY_PLUS_SIGN = "+" + CHAR_THIN_SPACE;
    public static final String CURRENCY_MINUS_SIGN = "-" + CHAR_THIN_SPACE;
    public static final String PREFIX_ALMOST_EQUAL_TO = Character.toString(CHAR_ALMOST_EQUAL_TO) + CHAR_THIN_SPACE;
    public static final int ADDRESS_FORMAT_GROUP_SIZE = 4;
    public static final int ADDRESS_FORMAT_LINE_SIZE = 12;

    public static final int BTC_MAX_PRECISION = 8;
    public static final int MBTC_MAX_PRECISION = 5;
    public static final int LOCAL_PRECISION = 4;

    public static final String DONATION_ADDRESS = "M8nK3eFCcCno71RnkGJR4qd61h2agjvS9A";
    public static final String REPORT_EMAIL = "@";
    public static final String REPORT_SUBJECT_ISSUE = "Reported issue";
    public static final String REPORT_SUBJECT_CRASH = "Crash report";

    public static final String LICENSE_URL = "http://www.gnu.org/licenses/gpl-3.0.txt";
    public static final String SOURCE_URL = "";
    public static final String BINARY_URL = ""; //TODO
    public static final String CREDITS_BITCOINJ_URL = "";
    public static final String CREDITS_ZXING_URL = "http://code.google.com/p/zxing/";
    public static final String CREDITS_ICON_URL = "http://monacoin.com/";
    public static final String AUTHOR_TWITTER_URL = "";
    public static final String AUTHOR_GOOGLEPLUS_URL = "";
    public static final String COMMUNITY_GOOGLEPLUS_URL = ""; //TODO
    public static final String MARKET_APP_URL = "market://details?id=%s";
    public static final String WEBMARKET_APP_URL = "https://play.google.com/store/apps/details?id=%s";
    public static final String MARKET_PUBLISHER_URL = "market://search?q=pub:\"Maximilian Keller\"";

    public static final String VERSION_URL = "http://parasprite.net:8081/version";
    public static final int HTTP_TIMEOUT_MS = 15 * 1000;

    public static final String PREFS_KEY_LAST_VERSION = "last_version";
    public static final String PREFS_KEY_LAST_USED = "last_used";
    public static final String PREFS_KEY_BEST_CHAIN_HEIGHT_EVER = "best_chain_height_ever";
    public static final String PREFS_KEY_ALERT_OLD_SDK_DISMISSED = "alert_old_sdk_dismissed";
    public static final String PREFS_KEY_REMIND_BACKUP = "remind_backup";

    public static final String PREFS_KEY_CONNECTIVITY_NOTIFICATION = "connectivity_notification";
    public static final String PREFS_KEY_SELECTED_ADDRESS = "selected_address";
    public static final String PREFS_KEY_EXCHANGE_CURRENCY = "exchange_currency";
    public static final String PREFS_KEY_TRUSTED_PEER = "trusted_peer";
    public static final String PREFS_KEY_TRUSTED_PEER_ONLY = "trusted_peer_only";
    public static final String PREFS_KEY_LABS_BLUETOOTH_OFFLINE_TRANSACTIONS = "labs_bluetooth_offline_transactions";
    public static final String PREFS_KEY_BTC_PRECISION = "btc_precision";
    public static final String PREFS_DEFAULT_BTC_PRECISION = "4";
    public static final String PREFS_KEY_DISCLAIMER = "disclaimer";

    // public static final long LAST_USAGE_THRESHOLD_JUST_MS = DateUtils.HOUR_IN_MILLIS;
    // public static final long LAST_USAGE_THRESHOLD_RECENTLY_MS = 2 * DateUtils.DAY_IN_MILLIS;

    public static final int SDK_JELLY_BEAN = 16;

    public static final int MEMORY_CLASS_LOWEND = 48;

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");

    public static final String MULTIMONA_URL = "http://monapu.github.io/multimona-site/";
}

