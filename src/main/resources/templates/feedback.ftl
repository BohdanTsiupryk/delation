<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <#if role == "ADMIN" || role == "MODER">

        <ul>
            <li><i>ID</i> -  ${feedback.id()}<hr></li>

            <li><i>type</i> - <b>${feedback.type()}</b><hr></li>
            <li><i>text</i> - ${feedback.text()}<hr></li>
            <li><i>author</i> - ${feedback.author()}<hr></li>
            <li><i>assigned</i>:
                <form method="post" action="/moder/feedback/assign">
                    <select name="moder">
                        <#list moders as mod>
                            <option value="none" selected>none</option>
                            <option value="${mod.id()}" <#if mod.name() == feedback.moder()>selected</#if>>${mod.name()}</option>
                        </#list>
                    </select>
                    <input type="hidden" name="id" value="${feedback.id()}">
                    <input type="submit" value="assign">
                </form>
                <hr>
            </li>
            <li><i>status</i>    - ${feedback.status()}<hr></li>
            <li><i>mentions</i>: <br><#list feedback.mentions() as m>@${m}<#sep><br></#list><hr></li>
            <li><i>created</i>   - ${feedback.date()?string("HH:mm:ss dd/MM/yyyy")}<hr></li>
        </ul>

    </#if>


</@c.page>