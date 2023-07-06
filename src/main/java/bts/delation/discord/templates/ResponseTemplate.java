package bts.delation.discord.templates;

public interface ResponseTemplate {

    String UNLINKED = "Ви не підключили акаунт до діскорду, \nпідключіть для продовження =)";
    String MOAN = "Ваш відгук додано, дякую, що робите нас кращими";
    String STATUS_TASK_LIST = "Відгуки: \n";
    String FEEDBACK_RESPONSE = "%s прийнято, дякуємо що робите нас кращими =) \n посилання на неї [туть](%s)";
    String HELP = """
            Бот допомагає проекту Bandercraft ставати кращим.
            Список доступних команд:
                feedback - дозволяє надіслати відгук, пропозицію чи поскаржитись на гравця чи навіть модератора
                status   - перевірити чи нам не пофіг на ваші відгуки =)
                sync     - синхронрізовує ваші облікові записи
                help     - Допомога
            """.stripIndent();
}
