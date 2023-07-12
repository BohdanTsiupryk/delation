package bts.delation.discord.templates;

public interface ResponseTemplate {

    String UNLINKED = "Ви не підключили акаунт до діскорду, \nпідключіть для продовження =)";
    String MOAN = "Ваш відгук додано, дякую, що робите нас кращими";
    String STATUS_TASK_LIST = "Відгуки: \n";
    String FEEDBACK_RESPONSE = "%s прийнято, дякуємо що робите нас кращими =) \n посилання на неї [туть](%s)";
    String OUT_MESSAGE_LIMIT = "Привіт, ти так багато надсилаєш відгуків, що тобі точно потрібен відпочинок, а нам час для розглядання =)\n " +
            "Якщо тобі є що ще сказати, повертайся завтра, або стукай модераторам в особисті, вони точно тебе вислухають \n" +
            "(Або просто забанять)";
    String HELP = """
            Бот допомагає проекту Bandercraft ставати кращим.
            Список доступних команд:
                feedback - дозволяє надіслати відгук, пропозицію чи поскаржитись на гравця чи навіть модератора
                                                    Можете спробувати і на адміна(про результат ви дізнаєтесь)
                status   - перевірити чи нам не пофіг на ваші відгуки =)
                sync     - синхронрізовує ваші облікові записи
                help     - Допомога
            """.stripIndent();
}
