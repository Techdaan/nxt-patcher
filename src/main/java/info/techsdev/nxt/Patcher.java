package info.techsdev.nxt;

import info.techsdev.nxt.lzma.RSLZMAOutputStream;
import info.techsdev.nxt.util.Whirlpool;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;
import java.util.zip.CRC32;

/**
 * Patcher for the NXT client and launcher.
 * <p>
 * The patcher replaces the JS5 and Login RSA keys in the client based on the input parameters, and replaces the
 * launcher RSA keys and the launcher regex.
 * <p>
 * The patcher also generates a CRC, hash and outputs the generated RSA key combinations.
 */
public class Patcher {

    public static void main(String[] args) throws Exception {
        // Validate settings & make sure files exist
        if (Settings.PATCHED_REGEX.length() >= Settings.RUNESCAPE_REGEX.length()) {
            throw new IllegalArgumentException("Patched regex is larger than RuneScape regex (" + Settings.PATCHED_REGEX.length() + " vs " + Settings.RUNESCAPE_REGEX.length() + ")");
        }
        if (Settings.PATCHED_CONFIG_URI.length() >= Settings.RUNESCAPE_CONFIG_URI.length()) {
            throw new IllegalArgumentException("Patched config URI is larger than RuneScape's config URI(" + Settings.PATCHED_CONFIG_URI.length() + " vs " + Settings.RUNESCAPE_CONFIG_URI.length() + ")");
        }

        // Add C string terminators to the patched regex and patched config URIs so the lengths match
        int iterations = Settings.RUNESCAPE_REGEX.length() - Settings.PATCHED_REGEX.length();
        for (int i = 0; i < iterations; i++)
            Settings.PATCHED_REGEX = Settings.PATCHED_REGEX + '\0';
        iterations = Settings.RUNESCAPE_CONFIG_URI.length() - Settings.PATCHED_CONFIG_URI.length();
        for (int i = 0; i < iterations; i++)
            Settings.PATCHED_CONFIG_URI += '\0';

        // Confirm the sizes are correct now (Sanity check)
        if (Settings.PATCHED_REGEX.length() != Settings.RUNESCAPE_REGEX.length()) {
            throw new IllegalArgumentException("Patched regex and RuneScape regex do not match in length (" + Settings.PATCHED_REGEX.length() + " vs " + Settings.RUNESCAPE_REGEX.length() + ")");
        }
        if (Settings.PATCHED_CONFIG_URI.length() != Settings.RUNESCAPE_CONFIG_URI.length()) {
            throw new IllegalArgumentException("Patched config URI and RuneScape config URI do not match in length (" + Settings.PATCHED_CONFIG_URI.length() + " vs " + Settings.RUNESCAPE_CONFIG_URI.length() + ")");
        }

        // Check if files exist
        Path originalLauncher = Settings.BASE_PATH.resolve("launcher.exe");
        Path originalClient = Settings.BASE_PATH.resolve("client.exe");

        if (!Files.exists(originalClient) || !Files.exists(originalLauncher)) {
            throw new FileNotFoundException("Ensure 'launcher.exe' and 'client.exe' exist in folder: " + Settings.BASE_PATH.toString() + " (This path is configurable in the Settings class)");
        }

        // Load data from disk
        byte[] launcherData = Files.readAllBytes(originalLauncher);
        byte[] clientData = Files.readAllBytes(originalClient);

        byte[] oldLauncherRSA = new byte[1024];
        byte[] oldJS5RSA = new byte[1024];
        byte[] oldLoginRSA = new byte[256];

        // Find launcher RSA keys
        int index = indexOf(launcherData, "10001\0".getBytes(Charset.forName("ASCII")));
        System.arraycopy(launcherData, index - 1028, oldLauncherRSA, 0, oldLauncherRSA.length);

        try {
            new BigInteger(new String(oldLauncherRSA, Charset.forName("ASCII")), 16);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Couldn't extract launcher RSA key from binary!");
        }

        // Find client RSA keys
        index = indexOf(clientData, "10001\0".getBytes(Charset.forName("ASCII")));
        System.arraycopy(clientData, index + 20, oldJS5RSA, 0, oldJS5RSA.length);
        System.arraycopy(clientData, index + 20 + oldJS5RSA.length + 16, oldLoginRSA, 0, oldLoginRSA.length);

        try {
            new BigInteger(new String(oldJS5RSA, Charset.forName("ASCII")), 16);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Couldn't extract JS5 RSA key from binary!");
        }


        try {
            new BigInteger(new String(oldLoginRSA, Charset.forName("ASCII")), 16);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Couldn't extract login RSA key from binary!");
        }

        // Load RSA keys
        loadOrGenerateKeys();

        // Patch RSA keys


        if (!patch(launcherData, oldLauncherRSA,
                Settings.RSAKeys.LAUNCHER_MODULUS.toString(16).getBytes(Charset.forName("ASCII")))) {
            throw new NullPointerException("Couldn't patch launcher RSA key - Did the offset change? Attempting to " +
                    "replace RSA key: " + new String(oldLauncherRSA, Charset.forName("ASCII")));
        }
        if (!patch(clientData, oldJS5RSA,
                Settings.RSAKeys.JS5.toString(16).getBytes(Charset.forName("ASCII")))) {
            throw new NullPointerException("Couldn't patch launcher RSA key - does the key in the Settings class " +
                    "match the one in the launcher?");
        }
        if (!patch(clientData, oldLoginRSA,
                Settings.RSAKeys.LOGIN.toString(16).getBytes(Charset.forName("ASCII")))) {
            throw new NullPointerException("Couldn't patch launcher RSA key - does the key in the Settings class " +
                    "match the one in the launcher?");
        }

        // Patch regex and default config uri
        if (!patch(launcherData, Settings.RUNESCAPE_REGEX.getBytes(Charset.forName("ASCII")),
                Settings.PATCHED_REGEX.getBytes(Charset.forName("ASCII")))) {
            throw new NullPointerException("Couldn't patch launcher regex - did you change the RuneScape regex?");
        }
        if (!patch(launcherData, Settings.RUNESCAPE_CONFIG_URI.getBytes(Charset.forName("ASCII")),
                Settings.PATCHED_CONFIG_URI.getBytes(Charset.forName("ASCII")))) {
            throw new NullPointerException("Couldn't patch launcher config uri - did you change the RuneScape uri?");
        }

        // Write data
        Files.write(Settings.BASE_PATH.resolve("launcher_patched.exe"), launcherData);
        Files.write(Settings.BASE_PATH.resolve("client_patched.exe"), clientData);

        // Generate CRC & hash
        byte[] compressedClient = RSLZMAOutputStream.compress(clientData);
        Files.write(Settings.BASE_PATH.resolve("client_compressed.exe"), compressedClient);

        System.out.println("Client CRC: " + CRC(clientData));
        System.out.println("Client hash: " + hash(clientData, Settings.RSAKeys.LAUNCHER_MODULUS,
                Settings.RSAKeys.LAUNCHER));

        System.out.println("\nSave this console output to a file to ensure you don't lose this data. Don't forget to " +
                "generate new hashes using the generated launcher keys to prevent having to release a new launcher " +
                "every client update. You can define the keys to use in the 'info.techsdev.nxt.Settings.RSAKeys' " +
                "class");
        System.out.println("Replace 'download_crc_0' in the client configurations file with the CRC above");
        System.out.println("Replace 'download_hash_0' in the client configurations file with the hash above");
    }

    /**
     * Loads the keys from the {@link Settings.RSAKeys} class and applies them. If they do not exist, this method will
     * generate the RSA keys.
     */
    private static void loadOrGenerateKeys() throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException {
        System.out.println("Loading/generating keys...");

        // Launcher keys
        if (Settings.RSAKeys.LAUNCHER != null || Settings.RSAKeys.LAUNCHER_MODULUS != null) {
            if (Settings.RSAKeys.LAUNCHER == null || Settings.RSAKeys.LAUNCHER_MODULUS == null) {
                throw new IllegalStateException("Please ensure both launcher and launcher modulus keys are not null");
            }

            if (Settings.RSAKeys.LAUNCHER_MODULUS.bitLength() != 4096)
                throw new IllegalArgumentException("Launcher modulus should have a bit length of 4096! Generate new " +
                        "keys by leaving the fields empty.");

            if (Settings.RSAKeys.LAUNCHER.bitLength() != 4095)
                throw new IllegalArgumentException("Launcher exponent should have a bit length of 4095! Generate new " +
                        "keys by leaving the fields empty.");

            System.out.println("Using pre-defined launcher keys [exponent]: BigInteger(" + Settings.RSAKeys.LAUNCHER.toString(16) + ")");
            System.out.println("Using pre-defined launcher keys [modulus ]: BigInteger(" + Settings.RSAKeys.LAUNCHER_MODULUS.toString(16) + ")");
        } else {
            RSAPrivateKeySpec launcher = generateKeySpec(4096);
            Settings.RSAKeys.LAUNCHER_MODULUS = launcher.getModulus();
            Settings.RSAKeys.LAUNCHER = launcher.getPrivateExponent();

            System.out.println("public static final BigInteger LAUNCHER_MODULUS = new BigInteger(\"" + launcher.getModulus().toString(16) + "\", 16);");
            System.out.println("public static final BigInteger LAUNCHER_EXPONENT = new BigInteger(\"" + launcher.getPrivateExponent().toString(16) + "\", 16);\n");
        }

        // JS5 key
        if (Settings.RSAKeys.JS5 != null) {
            if (Settings.RSAKeys.JS5.bitLength() != 4096)
                throw new IllegalArgumentException("JS5 modulus should have a bit length of 4096! Generate new keys " +
                        "by leaving the field empty.");

            System.out.println("Using pre-defined JS5 key: BigInteger(" + Settings.RSAKeys.JS5.toString(16) + ")");
        } else {
            RSAPrivateKeySpec js5 = generateKeySpec(4096);

            Settings.RSAKeys.JS5 = js5.getModulus();
            System.out.println("public static final BigInteger JS5_MODULUS = new BigInteger(\"" + js5.getModulus().toString(16) + "\", 16);");
            System.out.println("public static final BigInteger JS5_EXPONENT = new BigInteger(\"" + js5.getPrivateExponent().toString(16) + "\", 16);\n");
        }

        // Login key
        if (Settings.RSAKeys.LOGIN != null) {
            if (Settings.RSAKeys.LOGIN.bitLength() != 4096)
                throw new IllegalArgumentException("JS5 modulus should have a bit length of 4096! Generate new keys " +
                        "by leaving the field empty.");

            System.out.println("Using pre-defined login key: BigInteger(" + Settings.RSAKeys.LOGIN.toString(16) + ")");
        } else {
            RSAPrivateKeySpec login = generateKeySpec(1024);

            Settings.RSAKeys.LOGIN = login.getModulus();
            System.out.println("public static final BigInteger JS5_MODULUS = new BigInteger(\"" + login.getModulus().toString(16) + "\", 16);");
            System.out.println("public static final BigInteger JS5_EXPONENT = new BigInteger(\"" + login.getPrivateExponent().toString(16) + "\", 16);\n");
        }
    }

    /**
     * Calculates the NXT launcher hash for a client or file
     *
     * @param data     The data of the client or file (Uncompressed) to generate the hash for
     * @param modulus  The modulus of the launcher's RSA key
     * @param exponent The <b>private</b> exponent of the launcher's RSA key
     * @return The hash for the client or file
     */
    private static String hash(byte[] data, BigInteger modulus, BigInteger exponent) {
        // Generate the basic hash first - a 10-byte followed by the whirlpool of the client
        byte[] hash = new byte[65];
        hash[0] = 10;
        System.arraycopy(Whirlpool.getHash(data, 0, data.length), 0, hash, 1, 64);

        // RSA-encrypt the hash with the newly-patched RSA keys
        hash = new BigInteger(hash).modPow(exponent, modulus).toByteArray();

        // Base-64 encrypt the hash using Jagex' implementation (* instead of +, / instead of -) as well as removing
        // all ='s
        return Base64.getEncoder().encodeToString(hash).replaceAll("\\+", "\\*").replaceAll("/", "\\-").replaceAll(
                "=", "");
    }

    /**
     * Calculates the CRC checksum of a byte array
     *
     * @param data The data to get the CRC checksum from
     * @return The CRC checksum of the file
     */
    private static long CRC(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data, 0, data.length);
        return crc.getValue();
    }

    /**
     * Replaces a sequence of bytes in a byte array
     *
     * @param data   The data array to replace a byte sequence in
     * @param search The bytes to replace
     * @param patch  The bytes to replace the old bytes with
     * @return Whether if anything was patched. If this returns false, 'search' could not be found in the file
     */
    private static boolean patch(byte[] data, byte[] search, byte[] patch) {
        int index = indexOf(data, search);
        if (index == -1) {
            return false;
        }

        // Patch binary data
        for (int x = index; x < index + patch.length; x++) {
            data[x] = patch[x - index];
        }

        return true;
    }

    /**
     * Finds the beginning index of the search array in the data array. This returns the first occurence of the data
     *
     * @param data   The data array to search in
     * @param search The bytes to get the index from
     * @return -1 if the sequence was not found, otherwise the index wil be returned where the search bytes first occur
     */
    private static int indexOf(byte[] data, byte[] search) {
        outer:
        for (int i = 0; i < data.length - search.length + 1; i++) {
            for (int j = 0; j < search.length; j++) {
                if (data[i + j] != search[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    /**
     * Generates a RSA private key spec based on a size. This uses Jagex' modulus of "10001".
     *
     * @param size The size of the modulus of the key pair to generate
     * @return The generated RSA key pair
     */
    private static RSAPrivateKeySpec generateKeySpec(int size) throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(new RSAKeyGenParameterSpec(size, new BigInteger("10001", 16)));

        return factory.getKeySpec(generator.genKeyPair().getPrivate(), RSAPrivateKeySpec.class);
    }

}
