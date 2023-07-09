package bts.delation.uril;

public class DiscordUtil {

    public static String buildLinkPlaceholder(String placeholder, String link) {
        return "[" +
                placeholder +
                "](" +
                link +
                ")";
    }
}
