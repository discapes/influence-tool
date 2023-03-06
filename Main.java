import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
        // System.out.println("Hashing " + collection.toString());
        StringBuilder sb = new StringBuilder();
        for (String str : collection) {
            sb.append(str);
        }
        sb.append(MAGIC);
        String sb2 = sb.toString();
        return hashInner(sb2, collection.size());
    }

    /* renamed from: d */
    public static String hash(String... strArr) {
        return createHash(Arrays.asList(strArr));
    }

    public static String modifyTurnData(String base64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64);

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
        return Base64.getEncoder().encodeToString(bytes2);
    }

    public static Map<String, String> parseHeaders(String headerLine) {
        String[] matches = Pattern.compile("b'(.*?)'")
                .matcher(headerLine)
                .results()
                .map(m -> m.group((1)))
                .toArray(String[]::new);

        Map<String, String> headers = new HashMap<>();
        for (int i = 0; i < matches.length; i += 2) {
            headers.put(matches[i], matches[i + 1]);
        }
        return headers;
    }

    public static String match(String data, String regex) {
        Matcher m = Pattern.compile(regex).matcher(data);
        if (!m.find())
            return null;
        return m.group(1);
    }

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(new File("request.txt"));
        String headerLine = scanner.nextLine();
        String contentLine = scanner.nextLine();

        String turnData = URLDecoder.decode(match(contentLine, "turnData=(.*?)&"), StandardCharsets.UTF_8);
        Map<String, String> headers = parseHeaders(headerLine);

        String newTurnData = modifyTurnData(turnData);
        String newContent = contentLine.replaceAll("turnData=.*?&",
                "turnData=" + URLEncoder.encode(newTurnData, StandardCharsets.UTF_8) + "&");

        assert (headers.get("hash").equals(hash(headers.get("uid"), headers.get("lang"), headers.get("version"),
                headers.get("time"), contentLine)));
        String newHash = hash(headers.get("uid"), headers.get("lang"), headers.get("version"),
                headers.get("time"), newContent);

        System.out.println();
        System.out.println(newTurnData);
        System.out.println(newHash);
    }
}