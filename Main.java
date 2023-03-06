import java.util.zip.*;
import java.io.*;
import java.util.*;
import java.nio.charset.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.*;

public class Main {
    public static String MAGIC = "Ez6z3k1V477xY73D57I3m23L";

    public static String hashInner(String str, int i) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 255);
                if (hexString.length() == 1) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
            char[] charArray = sb.toString().toCharArray();
            char[] charArray2 = MAGIC.toCharArray();
            while (i2 < charArray.length) {
                charArray[i2] = charArray2[i2 % charArray2.length];
                i2 += i;
            }
            return new String(charArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: c */
    public static String createHash(Collection<String> collection) {
        StringBuilder sb = new StringBuilder();
        for (String str : collection) {
            sb.append(str);
        }
        sb.append(MAGIC);
        String sb2 = sb.toString();
        PrintStream printStream = System.out;
        printStream.println("Hash input\n" + sb2);
        return hashInner(sb2, collection.size());
    }

    /* renamed from: d */
    public static String hash(String... strArr) {
        return createHash(Arrays.asList(strArr));
    }

    public static void main(String args[]) throws IOException {

        String hash = hash("g16003158497410364065", "en", "302032", "1678117866544",
                "matchId=334548&turnData=H4sIAAAAAAAA%2F7WTuw6CMBSG3%2BXMHXoxMXTTOBviahwqPUUjiCklDoR3txgSE7l0AMbT%2F%2FTLd3qpQTmnksdBOQXyXHclyBoSzLJjlV%2FRgow2BLTKVYqxLXSVoAbJCbyKN9qdcW0L68o9msKijxu%2FBQ0%2BS%2BzRRJ9Gp2nU08w9vbkTllXmfENDxmQZXdSW0ZV12QB%2Fhi7j6%2BryZXX5yrpigD9DV7AldC9dy%2B%2FbaR23CyC35P86v8ON5lRM5lEg3gTogZxN4%2F1jmc5FYDoeyIU%2Fy%2BYD7lEGnsUEAAA%3D&timestamp=1678117183");
        System.out.println(hash);

        if (true)
            return;
        String str = "H4sIAAAAAAAAALWTPQ%2BCMBCG%2F8vNDP1woZvG2RBX41DpFY0gppQ4EP67hZCYCLQDMLbv3ZPn%2BtGAtFamz6O0EsSlGZYgGkgxz091cUMDIt5FoGQhM0xMqeoUFQgWwbv8oNlr25XQYXlAXRp0cetaUOOrwhGNj2nETyOOph%2FZ3Z6xqnPrCtpoTpaSVW0p2ViXTvAX6FK2rS5bV5dtrMsn%2BAt0OV1D9zqU%2FL6dUkm30Xf%2FXWc%2F3GxOuDePA%2FEuQA%2Fk1I93j8Wf88B0LJBzd5btF6N4ZObFBAAA";

        byte[] bytes = Base64.getDecoder().decode(URLDecoder.decode(str, StandardCharsets.UTF_8));

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        GZIPInputStream gzis = new GZIPInputStream(bais);
        String readed = "";
        int c;
        while ((c = gzis.read()) != -1)
            readed += (char) c;

        readed = readed.replaceAll("(\"addPower\":).", "$17");
        System.out.println(readed);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = new GZIPOutputStream(baos);
        gzos.write(readed.getBytes());
        gzos.close();
        byte[] bytes2 = baos.toByteArray();
        baos.close();
        System.out.println(URLEncoder.encode(Base64.getEncoder().encodeToString(bytes2), StandardCharsets.UTF_8));
    }
}