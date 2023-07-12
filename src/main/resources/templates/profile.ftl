<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>
<#import "parts/security.ftl" as sec>


<@c.page>
    <#assign sameUser = (principal.id == prof.id())>

    <div class="container">
        <h1>Profile Information</h1>

        <div class="card">
            <div class="card-body">
                <p><strong>ID:</strong>  ${prof.id()}</p>
                <p><strong>Email:</strong>  ${prof.email()}</p>
                <p><strong>User Role:</strong>  ${prof.role()}</p>
                <p><strong>SYNCED WITH DISCORD:</strong>
                    <#if prof.synced()>
                        <b style="color: green">Профіль підключений до Discord</b><br>
                    <#else>
                        <#if discordCode??><i>Введіть цей код нашому боту, через команду <b>/sync</b></i>

                            <div <#if !sameUser> id="hidden" </#if> >
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
                </p>
                <p><strong>SYNCED WITH MINE:</strong>
                    <#if prof.mineSynced()>
                        <b style="color: green">Профіль синхронізований з майнкрафтом</b><br>
                    <#else>
                        <b style="color: red">Схоже ваш профіль не синхронізований з майнкрафтом <a <#if !sameUser> id="hidden" </#if>
                                    href="https://bcraft.fun/accounts/profile/">Connect</a></b><br>
                    </#if>
                </p>
                <p><strong>Discord ID:</strong> ${prof.discordId()}</p>
                <p><strong>Discord Username:</strong> ${prof.discordUsername()}</p>
                <p><strong>Discord Mine Username:</strong> ${prof.mineUsername()}</p>
            </div>
        </div>
    </div>
    <hr>
    <@sec.show delation??>
        <ul class="list-group">
            <#list delation as d>
                <li class="list-group-item">${d.id()} - ${d.type()} - ${d.status()}</li>
            </#list>
        </ul>
    </@sec.show>

</@c.page>