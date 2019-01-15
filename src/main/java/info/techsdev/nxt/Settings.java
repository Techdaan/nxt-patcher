package info.techsdev.nxt;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple settings file to configure the rest of the patcher
 */
public class Settings {

    /**
     * The RSA key in the launcher, this should be extracted from the launcher since it's used to replace the key in
     * the launcher
     */
    public static final String LAUNCHER_RSA = "a49962fc0737fddcd94c0daf84e5d214a130b4eda82167e0e9507e1ca421b5001" +
            "75bf6b09c9ea9a4f3cdba0c8da28c696877e13244aa3baa53e522865db2b2148e" +
            "1a69d7e95567dcb88cbce828658ea0487cbd85d348aa48457f3f968d101ba2525" +
            "4a43e1b2b5051e9f5a3184a2dec6c59eb5b2ace3719d46feec4a82801b0f71b79" +
            "c36e6547cf0e22093a519703c9becdc829c749610b3fac188ac9811eba029fbaf" +
            "13aec78513957f7d4d3fd57c90bfcce0922cdce549e6dec3de0ce7c6b3b40fefd" +
            "54f6ebd844c852100174f0e4b43386e49d8f1283b74940a9a7915681ffdc7853d" +
            "37be466926aa1aa3a4940aed39b5a9b767b85035c28802dc82b391b3797240bb2" +
            "c57522498f4ef0801e6a28147f4c6d47b506417926e07e53bf9ef5a97a517cc7c" +
            "f2b00cceb1dc4842175a86710cd5dae9b89114bbc41842d92a8c7dc20d3994511" +
            "fe60f03c8137f2b9c25082f3ceaf80307187c785ec1d3f1750b29082f42ce4d99" +
            "9279cbb9f196436c61913d46c4242f939dc52d1f31e1ed5a1058cad63ebd6f331" +
            "14eb48b343ef6ed3a2dcd2d2e02ab1c94ee2f504a14c612f415181d8e5958197d" +
            "a90223bd577cebf948a5adc79a642c1c437365d49f4ac170526ca36d9c2057e02" +
            "aa96a3ade6bc8d1d0dce8f5d9205e361094009adcd1478393274d0c6b71453aa0" +
            "b27f5ed187c01b9f5d9573d1253f2b90099ddda628838fee9";

    /**
     * The JS5 key that is in the client, this should be extracted from the client since it's used to replace the key
     * in the client
     */
    public static final String JS5_RSA = "8a1887403b31d4a0da5771e17b6fc7919a97d3fa2f37a72cc423e9de192c48db4" +
            "7d7f51d0f0ff1fad7db69d971e6ae1160fd9f9ca20159c0b14a66cfe94d421165" +
            "684365bd4c1b81c97693b81fd0aa84f3ce6447617c49f6a8121558177f272df61" +
            "ee50952b782039f334d0b177b1e60d5c439278a14045cbd741905a1119ce75108" +
            "81cf510758ee4bd652a940ca5780bcc93053a245524399f70414b65d47c4dc583" +
            "92ae10cfa5416f042451401a81e76b6e66eda003d54d7d6e771a938f9fd6e7c51" +
            "8c278d59e010edcc0b87a2d4eb961a800d9d2a09505afacfb3d79dfde59e16ba6" +
            "33555d8706ee9e76ac906e6d1339d7c84aed2438308a0fcd0b115a5502f08de36" +
            "910d311dcae7824a3c9d6b0eb95f279ffd927f7bce2a33bf7d41f4d145ec46795" +
            "f63ef393d5ecc69c787b74d4567724f28e821dbd34211c65decf7dac0968f003b" +
            "4a0c3bc42f0f37e07773e4b73894a4910ba67517bfd9a555fca536613d6871960" +
            "17782adb102b56e7bf1b825bc2380e1b3cad789597bd7f973dc7cc1c02cae2caf" +
            "e672d86c751bb4fce8ae7305cfe464004beb551aa31cc0ac9750fd5d26985c6cc" +
            "14de567eba56bb71c788d73419dfe198fc02264c8243611e0f29620a65c984dc7" +
            "0a2a1baa912716ed2f82a4ad1595416e96b979b94dc09ce83f07920e35b31b8f2" +
            "7efa41c37dadb9ec9a6bb95031465679aa72ce907811a95cf";

    /**
     * The login RSA key that is in the client,  this should be extracted from the client since it's used to replace
     * the key in the client
     */
    public static final String LOGIN_RSA = "d5ac8b7aad359d915981e529bcd503e95c1eb5328b208e878d6149fa439990a86" +
            "a622ff0c5efb1a67aff90f981d4ceee37473b37fae95bbb38f691b23fda673a08" +
            "614b17fdc56a9d056fd3e58182d70d51b224157a3410e01f3f3a42148e5d7aa7a" +
            "1db7cb4da38bb1b3219df0e6b26a26370eb72b8792745acbaf6ef233188b9";

    /**
     * The regex in the launcher to see if websites are "legal" or not
     */
    public static final String RUNESCAPE_REGEX = "^https?://[a-z0-9\\-]*\\.?runescape.com(:[0-9]+)?/\0";

    /**
     * The regex we should patch. The string should be shorter than RuneScape's Regex
     */
    public static String PATCHED_REGEX = "^.*";

    /**
     * The RuneScape config URI where the game configuration is loaded from by default
     */
    public static final String RUNESCAPE_CONFIG_URI = "http://www.runescape.com/k=5/l=$(Language:0)/jav_config.ws\0";

    /**
     * The patched config URI where the game configuration should be loaded from instead. The string should be
     * shorter than RuneScape's config URI
     */
    public static String PATCHED_CONFIG_URI = "http://localhost/jav_config.ws";

    /**
     * The base path to patch files from
     */
    public static final Path BASE_PATH = Paths.get("library");

}
