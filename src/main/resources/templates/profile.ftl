<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>

    <ul>
        <li><i>ID</i> - ${prof.id()}
            <hr>
        </li>
        <li><i>EMAIL</i> - ${prof.email()}
            <hr>
        </li>
        <li><i>ROLE</i> - ${prof.role()}
            <hr>
        </li>
        <li><i>SYNCED WITH DISCORD</i>
            <#if prof.synced()>
                <b style="color: green">Профіль підключений до Discord</b><br>

            <#else>
                <#if discordCode??><i>Введіть цей код нашому боту, через команду <b>/sync</b></i>

                    <div>
                        <pre>
                          <code>
                               ${discordCode}
                          </code>
                        </pre>
                    </div>
                <#else>
                    <b style="color: red">Створити код для Discord <a href="/profile/createDiscordCode">Create</a></b>
                    <br>
                </#if>
            </#if>
            <hr>
        </li>
        <li><i>SYNCED WITH MINE</i>
            <#if prof.mineSynced()>
                <b style="color: green">Профіль синхронізований з майнкрафтом</b><br>
            <#else>
                <b style="color: red">Схоже ваш профіль не синхронізований з майнкрафтом <a
                            href="https://bcraft.fun/accounts/profile/">Connect</a></b><br>
            </#if>
            <hr>
        </li>
        <li><i>DISCORD ID</i> - ${prof.discordId()}
            <hr>
        </li>
        <li><i>DISCORD USERNAME</i> - ${prof.discordUsername()}
            <hr>
        </li>
        <li><i>DISCORD MINE USERNAME</i> - ${prof.mineUsername()}
            <hr>
        </li>
    </ul>
    <hr>
    <#if delation??>
        <ul>
            <#list delation as d>
                <li>${d.id()} - ${d.type()} - ${d.status()}</li>
            </#list>
        </ul>
    </#if>

</@c.page>