package info.techsdev.nxt;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple settings file to configure the rest of the patcher
 */
public class Settings {

    /**
     * The regex in the launcher to see if websites are "legal" or not
     */
    public static final String RUNESCAPE_REGEX = "^https?://[a-z0-9\\-]*\\.?runescape.com(:[0-9]+)?/\0";
    /**
     * The RuneScape config URI where the game configuration is loaded from by default
     */
    public static final String RUNESCAPE_CONFIG_URI = "http://www.runescape.com/k=5/l=$(Language:0)/jav_config.ws\0";
    /**
     * The base path to patch files from
     */
    public static final Path BASE_PATH = Paths.get("library");
    /**
     * The regex we should patch. The string should be shorter than RuneScape's Regex
     */
    public static String PATCHED_REGEX = "^.*";
    /**
     * The patched config URI where the game configuration should be loaded from instead. The string should be
     * shorter than RuneScape's config URI
     */
    public static String PATCHED_CONFIG_URI = "http://localhost/jav_config.ws";

    /**
     * Holds data about what RSA keys to patch
     */
    public static class RSAKeys {

        /**
         * The JS5 public RSA key to patch the client with. Leave this "null" to generate one automatically
         */
        public static BigInteger JS5 = new BigInteger(
                "9029487934064fc7a4e0dd5879a6b78e0d65441653b2e63cacaf15ea64b097d1460a5490f8bedb4444c459cb5431e2dd2eeb0b7bd1b13e4c52c232ca0279cbe90933e781d9e6e267b509b207e31cd6ea22629e13c558a71213623409b3510bd6d345b6db53a217eede19baccc593935ea25760a153bb3e4bea2a45af00cc1df8a8ad6ce0521c0fe9fb09695ff14011bd7a441ff498ed444138de29925fcfaab1e00d6a5ec6c1c23f0c0f6163cc262c33cc6405dedae9d3b4de716d6603d4e6ae27259123b5e2ae9d9b96ef70b5dd5228cf4975391213dc209292ac2834803ba5e9cfc61fa353013dc476f5206af43f3289174ee8a278964fa08bbf8c5990769b5612b89de510c2b2174dd8953e6088f0c2bef38714c099b0d67c396962762641e00b59a1159b7027ec4a917ba06555a8d8f53e0d974864b2a175ffc0ed86ec324abdbb12116766dffdff54362b534c58f7a83f0b59dc85e2b468fea7fffa99cef6c53a43240482a3164ea697b7a278324703a2e9699bfcf7a02587fe077ce0d075b60270a3a6327401d65cf6d7b79103515349b634e86496edb10419f8db0f56c0d501b490c09bf6375f7360bb0c8a3795a36b8bd0bf5a88a35ec1e4c654704703b08777dba586ee3692bda4a97d9fbe95ffc964dd8b856c04b73beef41e87afe57e353248eed6e6be409e46813ea83126b000b8810eed460b3ee271a285d56d", 16);

        /**
         * The login public RSA key to patch the client with. Leave this "null" to generate one automatically
         */
        public static BigInteger LOGIN = new BigInteger(
                "a4d446e1bcfb4b40a97bf54261d93df08ca68b879780992c2543354c1910e4e5cbc389092886c058bb2cfc5bdc18f4af532f9a67c297576c602fcbb16285d94fa8953203033b8093a96386c70c47ebed685cfa2ee0099da71a90efd80c1002e33682df2881236dd3671f096d71765db3c89652ab510367535c6fda9d45886fa1", 16);

        /**
         * The launcher's modulus to use to generate hashes. Leave this "null" to generate one automatically
         */
        public static BigInteger LAUNCHER_MODULUS = new BigInteger("810334fc35487507cbf7fc4583af2aa21e6739458f40862747bfd55887e303d482249657f1cabc0a8cdf7d08f6f48ccbf353266a6a3b2a4af1b70e4951a687bcc5a78836e89a0d5b68b603b06268a3ddc36780093ebfe725a8451eb9705e31bc9b4a733dbbe46148b5de22aead82fbeb1a6fb469871cd4e68a24506b07aee22bb99bc09bed49398440df15b4057e30a8bba134cfc2cdb18c542fdc12ec0a3444255c5c9a1e76b7f2a8625fcfbd94c440e92aa1b4185a891ce433f4cf2b95054bc9983b4426c18c480f3a5046a3d6fc9ae0009ff989eb7dbd30543167eb17ca88cc8da467ae7fa892a8d2dc2d69b21447ddbc90565ebc1270b8a835164313429c8e80934f3e496163f8bd20b91dadee8a20a6200b48ed84b1301c879e2310f46f01232366f0dc5e87e79550f846c0212312dc775015f1c53ba9c848491578b07d433acda3b6fac2ba8b6f36e566865ca8a3bcf54cd631b8e309077a6d6e452bc954598f1803cd722dda1f06bfe3e0ac1dfb556d99350aaff2111efefde3174bdeffad6c75a41d13b689880f320b09fbe2bf51a0691fd4fbfc656f7834248a5a80fe31d4177b99a53ed28746dc75d38409f5e81ac854053b52752750a58c214ffc7fd75cf12c88b10ecea1813c1b4eb3bd47d7c7c3dcf407003732e4aa2e3e6d2dc0d1667c038bd5876e4187a5450bbe876ffc7644f56510f980f8d3273005bf07", 16);;

        /**
         * The launcher's public key to patch the launcher with. Leave this "null" to generate one automatically
         */
        public static BigInteger LAUNCHER = new BigInteger("155a5187b8cdf048c5129b576a0f30f03a35fd7dacd312212837f0f8c12ceab25e83edba4a93b281d08520edbdea05efafab407d766daa7753006845ec54b2bca4be30c65dc33998ba80fda550f45ec2d297abd25be7693b8b593d5795e89134d5430245e2764f3fb892368d50bc90337e61a4ab9ea83ff8943ea46cc28dc873523a1c2bf29f1efcebbbc09cd617e7b7d5ae3dce7140983550adc72ffe8f45cd4d9db6d9ef56d8e539c766723cbf85a7f4af90a9fd29e6edb83b7d3658d1465a1fd8a95e83479f8f41adf6216af6a7427ef6bb8392b73e4d74d54a84179e0d708ce298a66c744693578be6d3415e2f0b220116a6a4f15ddff84026e13362d9b051a056abdffcc36257d6220be598139f57037733626966b6d89203faba42f3053246003db974401ca223e50a1284f3135a376e879f6eb1fc2a00720af767681f0ee4083d3eaf62a604d70b464a2f9f3d246b0a775785125ece039369495b90a33f1e65b0b5a44f9699a9de2b445ad6e916aff7b6bbd42fd72d1e13f207911ed9fe210cdcc9bfdd9531136f8c52f75b94733ce60ecd80ea55c84f3cb20d86e04ef69e94bbd95af042924e65e0e334d5fd891e1852f59e1fc03a8a2781ecd12ff5fced02ba77ca55c831ddf80b7eeb7d8b77b6ef80dbd23dd254b833240343c3caefa81feebe1d4eb26bb3e16da38c8a80b377d90ebce3495d38c209b71fe8c361", 16);;

    }

}
