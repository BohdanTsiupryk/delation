<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <#if role == "ADMIN" || role == "MODER">

        <div>
            <a href="/moder/feedback">clear filters</a>
        </div>
        <div>
            <form method="get" action="/moder/feedback">
                <select name="type">
                    <#list types as type>
                        <option value="${type}">${type}</option>
                    </#list>
                </select>
                <input type="submit" value="filter">
            </form>
        </div>
        <table>
            <tr>
                <th>ID</th>
                <th>type</th>
                <th>text</th>
                <th>author</th>
                <th>assigned</th>
                <th>mentions</th>
                <th>status</th>
                <th>created</th>
                <th></th>
            </tr>
            <#list list as l>
                <tr>
                    <td title="${l.id()}">${l.id()?substring(0, 8)}...</td>
                    <td>${l.type()}</td>
                    <td class="text-limit" title="${l.text()}">${l.text()}</td>
                    <td>${l.author()}</td>
                    <td>
                        <form method="post" action="/moder/feedback/assign">
                            <select name="moder">
                                <#list moders as mod>
                                    <option value="none" selected>none</option>
                                    <option value="${mod.id()}"
                                            <#if mod.name() == l.moder()>selected</#if>>${mod.name()}</option>
                                </#list>
                            </select>
                            <input type="hidden" name="id" value="${l.id()}">
                            <input type="submit" value="assign">
                        </form>
                    </td>
                    <td><#list l.mentions() as m>@${m}<#sep>,</#list></td>
                    <td>${l.status()}</td>
                    <td>${l.date()?string("HH:mm:ss dd/MM/yyyy")}</td>
                    <td><a href="/moder/feedback/${l.id()}">open</a></td>
                </tr>
            </#list>

        </table>

    </#if>


</@c.page>